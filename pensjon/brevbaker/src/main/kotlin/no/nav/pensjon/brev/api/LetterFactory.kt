package no.nav.pensjon.brev.api

import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EttSaksbehandlervalgIDSLImpl
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.template.AlltidValgbartVedlegg
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brev.template.BrevbakerDSLInternal
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

    @OptIn(BrevbakerDSLInternal::class)
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

    @OptIn(BrevbakerDSLInternal::class)
    private fun oppdaterSaksbehandlervalg(
        template: LetterTemplate<*, BrevbakerBrevdata>,
        letterData: BrevbakerBrevdata,
    ): Map<String, EttSaksbehandlervalgIDSLImpl<*>> {
        val nyeVerdier = (letterData as? Map<*, *>)?.get("saksbehandlerValg") as? Map<*, *> ?: emptyMap<Any?, Any?>()
        return template.saksbehandlervalg.orEmpty().mapValues { (key, fraMalen) ->
            val nyVerdi = nyeVerdier[key]
            val verdi = when (fraMalen) {
                is SaksbehandlervalgVerdi.Bool -> nyVerdi as? Boolean
                is SaksbehandlervalgVerdi.Integer -> (nyVerdi as? Number)?.toInt() ?: (nyVerdi as? String)?.toIntOrNull()
                is SaksbehandlervalgVerdi.Text -> nyVerdi as? String
                is SaksbehandlervalgVerdi.Enum<*> -> (nyVerdi as? String)?.let { java.lang.Enum.valueOf(fraMalen.clazz, it) }
            }
            EttSaksbehandlervalgIDSLImpl(key, verdi, fraMalen)
        }
    }
}