package no.nav.pensjon.etterlatte

import io.ktor.http.ContentType
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.micrometer.core.instrument.Tag
import no.nav.pensjon.brev.Metrics
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.ParseLetterDataException
import no.nav.pensjon.brev.api.toLanguage
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.maler.AllTemplates
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.jacksonObjectMapper
import no.nav.pensjon.brev.template.render.LatexDocumentRenderer
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

class LetterResource(templates: AllTemplates, private val latexCompilerService: LaTeXCompilerService) {
    private val objectMapper = jacksonObjectMapper()

    private val autoBrevMap: Map<Brevkode<*>, AutobrevTemplate<BrevbakerBrevdata>> =
        templates.hentAutobrevmaler().associateBy { it.kode }

    private fun getTemplate(kode: Brevkode<*>) = autoBrevMap[kode]

    suspend fun renderPDF(brevbestilling: BestillBrevRequest<Brevkode.Automatisk>): LetterResponse =
        with(brevbestilling) {
            renderPDF(createLetter(kode, letterData, language, felles))
        }

    private suspend fun renderPDF(letter: Letter<BrevbakerBrevdata>): LetterResponse {
        val pdfBase64 = Letter2Markup.render(letter)
            .let { LatexDocumentRenderer.render(it.letterMarkup, it.attachments, letter) }
            .let { latexCompilerService.producePDF(it) }
        return LetterResponse(
            pdfBase64.base64PDF,
            ContentType.Application.Pdf.toString(),
            letter.template.letterMetadata
        )
    }

    fun renderJSON(brevbestilling: BestillBrevRequest<*>): LetterMarkup =
        with(brevbestilling) {
            renderJSON(createLetter(kode, letterData, language, felles))
        }

    private fun renderJSON(letter: Letter<BrevbakerBrevdata>) = Letter2Markup.render(letter).letterMarkup

    fun countLetter(kode: Brevkode.Automatisk) = Metrics.prometheusRegistry.counter(
        "pensjon_brevbaker_etterlatte_request_count",
        listOf(Tag.of("brevkode", kode.kode()))
    ).increment()


    private fun createLetter(
        brevkode: Brevkode<*>,
        brevdata: BrevbakerBrevdata,
        spraak: LanguageCode,
        felles: Felles,
    ): Letter<BrevbakerBrevdata> {
        val template =
            getTemplate(brevkode)?.template ?: throw NotFoundException("Template '${brevkode}' doesn't exist")

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

    private fun parseArgument(
        letterData: BrevbakerBrevdata,
        template: LetterTemplate<*, BrevbakerBrevdata>,
    ): BrevbakerBrevdata =
        try {
            objectMapper.convertValue(letterData, template.letterDataType.java)
        } catch (e: IllegalArgumentException) {
            throw ParseLetterDataException("Could not parse letterData: ${e.message}", e)
        }

}
