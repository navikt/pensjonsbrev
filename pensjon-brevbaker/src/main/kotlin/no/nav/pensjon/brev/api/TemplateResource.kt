package no.nav.pensjon.brev.api

import io.ktor.server.plugins.*
import io.micrometer.core.instrument.Tag
import no.nav.brev.brevbaker.BrevbakerHTML
import no.nav.brev.brevbaker.BrevbakerJSON
import no.nav.brev.brevbaker.BrevbakerPDF
import no.nav.pensjon.brev.Metrics
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

private val objectMapper = jacksonObjectMapper()

class TemplateResource<Kode : Brevkode<Kode>, out T : BrevTemplate<BrevbakerBrevdata, Kode>>(
    val name: String,
    templates: Set<T>,
    laTeXCompilerService: LaTeXCompilerService,
) {
    private val brevbakerPDF = BrevbakerPDF(laTeXCompilerService)

    private val templateLibrary: TemplateLibrary<Kode, T> = TemplateLibrary(templates)

    fun listTemplatesWithMetadata() = templateLibrary.listTemplatesWithMetadata()

    fun listTemplatekeys() = templateLibrary.listTemplatekeys()

    fun getTemplate(kode: Kode) = templateLibrary.getTemplate(kode)

    suspend fun renderPDF(brevbestilling: BestillBrevRequest<Kode>): LetterResponse =
        with(brevbestilling) {
            brevbakerPDF.lagPDF(createLetter(kode, letterData, language, felles))
        }

    suspend fun renderPDF(brevbestilling: BestillRedigertBrevRequest<Kode>): LetterResponse =
        with(brevbestilling) {
            brevbakerPDF.lagPDF(createLetter(kode, letterData, language, felles), letterMarkup)
        }

    fun renderHTML(brevbestilling: BestillBrevRequest<Kode>): LetterResponse =
        with(brevbestilling) {
            BrevbakerHTML.renderHTML(createLetter(kode, letterData, language, felles))
        }

    fun renderHTML(brevbestilling: BestillRedigertBrevRequest<Kode>): LetterResponse =
        with(brevbestilling) {
            BrevbakerHTML.renderHTML(createLetter(kode, letterData, language, felles), letterMarkup)
        }

    fun renderJSON(brevbestilling: BestillBrevRequest<Kode>): LetterMarkup =
        with(brevbestilling) {
            BrevbakerJSON.renderJSON(createLetter(kode, letterData, language, felles))
        }

    fun renderLetterMarkup(brevbestilling: BestillBrevRequest<Kode>): LetterMarkup =
        createLetter(brevbestilling.kode, brevbestilling.letterData, brevbestilling.language, brevbestilling.felles)
            .let { Letter2Markup.renderLetterOnly(it.toScope(), it.template) }

    fun countLetter(brevkode: Kode): Unit =
        Metrics.prometheusRegistry.counter(
            "pensjon_brevbaker_letter_request_count",
            listOf(Tag.of("brevkode", brevkode.kode()))
        ).increment()

    private fun createLetter(brevkode: Kode, brevdata: BrevbakerBrevdata, spraak: LanguageCode, felles: Felles): Letter<BrevbakerBrevdata> {
        val template = getTemplate(brevkode)?.template ?: throw NotFoundException("Template '${brevkode}' doesn't exist")

        val language = spraak.toLanguage()
        if (!template.language.supports(language)) {
            throw BadRequestException("Template '${brevkode}' doesn't support language: ${template.language}")
        }

        return Letter(
            template = template,
            argument = parseArgument(brevdata, template),
            language = language,
            felles = felles,
        )
    }


    private fun parseArgument(letterData: BrevbakerBrevdata, template: LetterTemplate<*, BrevbakerBrevdata>): BrevbakerBrevdata =
        try {
            objectMapper.convertValue(letterData, template.letterDataType.java)
        } catch (e: IllegalArgumentException) {
            throw ParseLetterDataException("Could not deserialize letterData: ${e.message}", e)
        }
}
