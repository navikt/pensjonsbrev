package no.nav.pensjon.brev.api

import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.converters.SaksbehandlervalgIDSLImpl
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
            // TODO: Trur dette er ein for enkel if. Utfordringa er at vi ikkje veit typen på letterData her utan reflection, det er eit enkelt map
            if (template.saksbehandlervalg.isNotEmpty()) {
                val saksbehandlervalg = mutableMapOf<String, SaksbehandlervalgVerdi>()
                saksbehandlervalg.putAll(template.saksbehandlervalg)
                if (letterData is Map<*,*> && letterData.containsKey("saksbehandlerValg")) {
                    val nyeSaksbehandlervalg = letterData["saksbehandlerValg"] as Map<String, Any?>
                    nyeSaksbehandlervalg.entries.forEach { nye ->
                        val nyKey = nye.key.replace(Regex("(?<!^)([A-Z])"), " $1").lowercase().replaceFirstChar { it.uppercase() } // TODO: Dette tar frontend sin ukjentSamboer og gjer om til Ukjent samboer
                        when (nye.value) {
                            is Boolean -> saksbehandlervalg[nyKey] = SaksbehandlervalgVerdi.Bool(nye.value as Boolean)
                            is Int -> saksbehandlervalg[nyKey] = SaksbehandlervalgVerdi.Integer(nye.value as Int)
//                            is Enum<*> -> saksbehandlervalg[it.key] = SaksbehandlervalgVerdi.Enum<Any>(it.value as Enum<String>) // TODO: typing
                            else -> throw IllegalArgumentException("Unsupported type for saksbehandlervalg: ${nye.value?.javaClass}")
                        }.let { saksbehandlervalg[nye.key] = saksbehandlervalg[nyKey]!! }
                    }
                }
                val newInstance: Any? = template.letterDataType.java.constructors.first().newInstance(EmptyFagsystemdata, SaksbehandlervalgIDSLImpl(saksbehandlervalg)) // TODO: Må hente og type pesysdata ordentleg her
                return (newInstance as BrevbakerBrevdata)
//                objectMapper.convertValue(mapOf(
//                    "pesysData" to (letterData as? Map<String, Any?> ?: emptyMap()),
//                    "saksbehandlerValg" to template.saksbehandlervalg
//                ), template.letterDataType.java)
            } else {
                objectMapper.convertValue(letterData, template.letterDataType.java)
            }
        } catch (e: IllegalArgumentException) {
            throw ParseLetterDataException("Could not deserialize letterData: ${e.message}", e)
        }
}