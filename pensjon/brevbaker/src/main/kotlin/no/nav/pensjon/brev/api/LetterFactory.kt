package no.nav.pensjon.brev.api

import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.api.model.maler.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequestV2
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.AlltidValgbartVedlegg
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.brevbakerJacksonObjectMapper
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.slf4j.LoggerFactory
import kotlin.time.measureTime

private val logger = LoggerFactory.getLogger(LetterFactory::class.java)

private val objectMapper = brevbakerJacksonObjectMapper()

internal fun varmOppBrevdata(maler: Collection<BrevTemplate<*, *>>) {
    val varighet = measureTime {
        maler.forEach { objectMapper.canDeserialize(objectMapper.constructType(it.template.letterDataType.java)) }
    }
    logger.info("Oppvarming av brevdata for ${maler.size} maler ferdig på ${varighet.inWholeMilliseconds} ms")
}

class LetterFactory<Kode: Brevkode<Kode>>(alltidValgbareVedlegg: Set<AlltidValgbartVedlegg<*>>) {
    private val vedleggLibrary = AlltidValgbartVedleggLibrary(alltidValgbareVedlegg)


    fun createLetter(brevbestilling: BestillBrevRequest<Kode>, template: BrevTemplate<BrevbakerBrevdata, out Brevkode<*>>?) =
        with(brevbestilling) { createLetter(template,kode, letterData, language, felles, listOf()) }

    fun createLetter(brevbestilling: BestillRedigertBrevRequest<Kode>, template: BrevTemplate<BrevbakerBrevdata, out Brevkode<*>>?) =
        with(brevbestilling) {
            createLetter(
                template,
                redigerbartBrev.kode,
                redigerbartBrev.letterData,
                redigerbartBrev.language,
                redigerbartBrev.felles,
                alltidValgbareVedlegg
            )
        }

    fun createLetter(brevbestilling: BestillRedigertBrevRequestV2<Kode>, template: BrevTemplate<BrevbakerBrevdata, out Brevkode<*>>?) =
        with(brevbestilling) {
            createLetter(
                template,
                redigerbartBrev.kode,
                redigerbartBrev.letterData,
                redigerbartBrev.language,
                redigerbartBrev.felles,
                alltidValgbareVedlegg
            )
        }

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
            objectMapper.convertValue(letterData, template.letterDataType.java)
        } catch (e: IllegalArgumentException) {
            throw ParseLetterDataException("Could not deserialize letterData: ${e.message}", e)
        }
}