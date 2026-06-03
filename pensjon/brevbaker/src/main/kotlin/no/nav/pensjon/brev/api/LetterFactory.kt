package no.nav.pensjon.brev.api

import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.template.AlltidValgbartVedlegg
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.brevbakerJacksonObjectMapper
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles
import no.nav.pensjon.brevbaker.api.model.LanguageCode

private val objectMapper = brevbakerJacksonObjectMapper()

class LetterFactory<Kode: Brevkode<Kode>>(alltidValgbareVedlegg: Set<AlltidValgbartVedlegg<*>>) {
    private val vedleggLibrary = AlltidValgbartVedleggLibrary(alltidValgbareVedlegg)


    fun createLetter(brevbestilling: BestillBrevRequest<Kode>, template: BrevTemplate<BrevbakerBrevdata, out Brevkode<*>>?) =
        with(brevbestilling) { createLetter(template,kode, letterData, language, felles, listOf()) }

    fun createLetter(brevbestilling: BestillRedigertBrevRequest<Kode>, template: BrevTemplate<BrevbakerBrevdata, out Brevkode<*>>?) =
        with(brevbestilling) { createLetter(template, kode, letterData, language, felles, alltidValgbareVedlegg) }

    private fun createLetter(
        brevTemplate: BrevTemplate<BrevbakerBrevdata, out Brevkode<*>>?,
        brevkode: Kode,
        brevdata: BrevbakerBrevdata,
        spraak: LanguageCode,
        felles: BrevbakerFelles,
        valgteVedlegg: List<AlltidValgbartVedleggKode>
    ): Letter<BrevbakerBrevdata> {
        val template =
            brevTemplate?.template ?: throw NotFoundException("Template '${brevkode}' doesn't exist")

        val language = spraak.toLanguage()
        if (!template.language.supports(language)) {
            throw BadRequestException("Template '${brevkode}' doesn't support language: ${template.language}")
        }

        val vedlegg = vedleggLibrary.getVedlegg(valgteVedlegg)
        vedlegg.forEach {
            require(it.kode.spraak.contains(spraak)) {
                "Vedlegg '${it.kode}' støtter ikke språk $spraak"
            }
        }

        @OptIn(InterneDataklasser::class)
        return LetterImpl(
            template = template.medEkstraVedlegg(vedlegg.map { it.asIncludeAttachment() }),
            argument = parseArgument(brevdata, template),
            language = language,
            felles = felles,
        )
    }

    private fun parseArgument(
        letterData: BrevbakerBrevdata,
        template: LetterTemplate<*, BrevbakerBrevdata>,
    ): BrevbakerBrevdata =
        try {
            val data = if (letterData is Map<*, *> && template.saksbehandlervalg.isNotEmpty()) {
                letterData.toMutableMap().also { it["saksbehandlerValg"] = oppdaterSaksbehandlervalg(template, letterData) }
            } else {
                letterData
            }
            return objectMapper.convertValue(data, template.letterDataType.java)
        } catch (e: IllegalArgumentException) {
            throw ParseLetterDataException("Could not deserialize letterData: ${e.message}", e)
        }

    private fun oppdaterSaksbehandlervalg(
        template: LetterTemplate<*, BrevbakerBrevdata>,
        letterData: BrevbakerBrevdata,
    ): Map<String, SaksbehandlervalgVerdi> {
        val saksbehandlervalg = mutableMapOf<String, SaksbehandlervalgVerdi>()
        saksbehandlervalg.putAll(template.saksbehandlervalg)
        if (letterData is Map<*, *> && letterData.containsKey("saksbehandlerValg")) {
            (letterData["saksbehandlerValg"] as Map<String, Any?>).entries.forEach { nye -> // TODO: kva er eigentleg typen her?
                saksbehandlervalg[nye.key] = when (val eksisterende = template.saksbehandlervalg[nye.key]) {
                    is SaksbehandlervalgVerdi.Bool -> SaksbehandlervalgVerdi.Bool(nye.value as? Boolean ?: false, nye.key)
                    is SaksbehandlervalgVerdi.Enum<*> -> eksisterende.kopier(java.lang.Enum.valueOf(eksisterende.clazz, nye.value as String))
                    is SaksbehandlervalgVerdi.Integer -> SaksbehandlervalgVerdi.Integer((nye.value as? String)?.toInt(), nye.key)
                    is SaksbehandlervalgVerdi.Text -> SaksbehandlervalgVerdi.Text(nye.value as String, nye.key)
                    null -> // Dette kan skje hvis malen kombinerer ny og gammel løsning
                        when (nye.value) {
                            // TODO: lurer på om vi kkal støtte det her i det heile tatt, innfører ein del ekstra kompleksitet
                            // Display text her blir i praksis ikke brukt, så det er ikke så farlig at den er uformatert
                            is Boolean -> SaksbehandlervalgVerdi.Bool(nye.value as Boolean, nye.key)
                            is Int -> SaksbehandlervalgVerdi.Integer(nye.value as Int, nye.key)
                            //is Enum<*> -> saksbehandlervalg[nye.key] = SaksbehandlervalgVerdi.Enum<*>(nye.value as Enum<String>) // TODO: typing
                            is String -> SaksbehandlervalgVerdi.Text(nye.value as String, nye.key)
                            else -> throw IllegalArgumentException("Unsupported type for saksbehandlervalg: ${nye.value?.javaClass}")
                        }
                }
            }
        }
        return saksbehandlervalg
    }
}