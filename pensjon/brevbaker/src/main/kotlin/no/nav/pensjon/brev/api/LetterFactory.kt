package no.nav.pensjon.brev.api

import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import no.nav.brev.InternKonstruktoer
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
            val data = if (letterData is Map<*, *> && template.saksbehandlervalg?.isNotEmpty() == true) {
                letterData.toMutableMap().also { it["saksbehandlerValg"] = oppdaterSaksbehandlervalg(template, letterData) }
            } else {
                letterData
            }
            return objectMapper.convertValue(data, template.letterDataType.java)
        } catch (e: IllegalArgumentException) {
            throw ParseLetterDataException("Could not deserialize letterData: ${e.message}", e)
        }

    @OptIn(InternKonstruktoer::class)
    private fun oppdaterSaksbehandlervalg(
        template: LetterTemplate<*, BrevbakerBrevdata>,
        letterData: BrevbakerBrevdata,
    ): Map<String, SaksbehandlervalgVerdi<*>> {
        val saksbehandlervalg = mutableMapOf<String, SaksbehandlervalgVerdi<*>>()
        template.saksbehandlervalg?.let { saksbehandlervalg.putAll(it) }
        if (letterData is Map<*, *> && letterData.containsKey("saksbehandlerValg")) {
            (letterData["saksbehandlerValg"] as Map<String, Any?>).entries.forEach { nye -> // TODO: kva er eigentleg typen her?
                saksbehandlervalg[nye.key] = when (val eksisterende = template.saksbehandlervalg?.get(nye.key)) {
                    is SaksbehandlervalgVerdi.Bool -> SaksbehandlervalgVerdi.Bool(nye.value as? Boolean ?: false, eksisterende.displayText)
                    is SaksbehandlervalgVerdi.Enum<*> -> eksisterende.withRawValue(nye.value)
                    is SaksbehandlervalgVerdi.Integer -> SaksbehandlervalgVerdi.Integer(
                        (nye.value as? Number)?.toInt() ?: (nye.value as? String)?.toIntOrNull(),
                        eksisterende.displayText
                    )
                    is SaksbehandlervalgVerdi.Text -> SaksbehandlervalgVerdi.Text(nye.value as? String, eksisterende.displayText)
                    null -> throw IllegalArgumentException("Saksbehandlervalg fins ikke, dette skal ikke skje: ${nye.key}, som har type ${nye.value?.javaClass}")
                }
            }
        }
        return saksbehandlervalg
    }
}

@OptIn(InternKonstruktoer::class)
private fun SaksbehandlervalgVerdi.Enum<*>.withRawValue(raw: Any?): SaksbehandlervalgVerdi.Enum<*> = SaksbehandlervalgVerdi.Enum(defaultValue = SaksbehandlervalgVerdi.Enum.parse(clazz, raw as? String), displayText = displayText, clazz = clazz)