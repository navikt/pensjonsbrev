package no.nav.pensjon.brev.api

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.micrometer.core.instrument.Tag
import no.nav.pensjon.brev.Metrics
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.HTMLDocumentRenderer
import no.nav.pensjon.brev.template.render.LatexDocumentRenderer
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brev.template.render.LetterWithAttachmentsMarkup
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import java.util.*

private val objectMapper = jacksonObjectMapper()
private val base64Decoder = Base64.getDecoder()

class TemplateResource<Kode : Enum<Kode>, out T : BrevTemplate<BrevbakerBrevdata, Kode>>(
    val name: String,
    templates: Set<T>,
    private val laTeXCompilerService: LaTeXCompilerService,
) {
    val templates: Map<Kode, T> = templates.associateBy { it.kode }

    fun getTemplate(kode: Kode) = templates[kode]

    suspend fun renderPDF(brevbestilling: BestillBrevRequest<Kode>): LetterResponse =
        with(brevbestilling) {
            renderPDF(createLetter(kode, letterData, language, felles))
        }

    suspend fun renderPDF(brevbestilling: BestillRedigertBrevRequest<Kode>): LetterResponse =
        with(brevbestilling) {
            renderPDF(createLetter(kode, letterData, language, felles), letterMarkup)
        }

    fun renderHTML(brevbestilling: BestillBrevRequest<Kode>): LetterResponse =
        with(brevbestilling) {
            renderHTML(createLetter(kode, letterData, language, felles))
        }

    fun renderHTML(brevbestilling: BestillRedigertBrevRequest<Kode>): LetterResponse =
        with(brevbestilling) {
            renderHTML(createLetter(kode, letterData, language, felles), letterMarkup)
        }

    fun renderLetterMarkup(brevbestilling: BestillBrevRequest<Kode>): LetterMarkup =
        createLetter(brevbestilling.kode, brevbestilling.letterData, brevbestilling.language, brevbestilling.felles)
            .let { Letter2Markup.renderLetterOnly(it.toScope(), it.template) }

    fun countLetter(brevkode: Kode): Unit =
        Metrics.prometheusRegistry.counter(
            "pensjon_brevbaker_letter_request_count",
            listOf(Tag.of("brevkode", brevkode.name))
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

    private suspend fun renderPDF(letter: Letter<BrevbakerBrevdata>, redigertBrev: LetterMarkup? = null): LetterResponse =
        renderCompleteMarkup(letter, redigertBrev)
            .let { LatexDocumentRenderer.render(it.letterMarkup, it.attachments, letter) }
            .let { laTeXCompilerService.producePDF(it) }
            .let { pdf ->
                LetterResponse(
                    file = base64Decoder.decode(pdf.base64PDF),
                    contentType = ContentType.Application.Pdf.toString(),
                    letterMetadata = letter.template.letterMetadata
                )
            }

    private fun renderHTML(letter: Letter<BrevbakerBrevdata>, redigertBrev: LetterMarkup? = null): LetterResponse =
        renderCompleteMarkup(letter, redigertBrev)
            .let { HTMLDocumentRenderer.render(it.letterMarkup, it.attachments, letter) }
            .let { html ->
                LetterResponse(
                    file = html.indexHTML.content.toByteArray(Charsets.UTF_8),
                    contentType = ContentType.Text.Html.withCharset(Charsets.UTF_8).toString(),
                    letterMetadata = letter.template.letterMetadata,
                )
            }

    private fun renderCompleteMarkup(letter: Letter<BrevbakerBrevdata>, redigertBrev: LetterMarkup? = null): LetterWithAttachmentsMarkup =
        letter.toScope().let { scope ->
            LetterWithAttachmentsMarkup(
                redigertBrev ?: Letter2Markup.renderLetterOnly(scope, letter.template),
                Letter2Markup.renderAttachmentsOnly(scope, letter.template),
            )
        }


    private fun parseArgument(letterData: Any, template: LetterTemplate<*, BrevbakerBrevdata>): BrevbakerBrevdata =
        try {
            objectMapper.convertValue(letterData, template.letterDataType.java)
        } catch (e: IllegalArgumentException) {
            throw ParseLetterDataException("Could not deserialize letterData: ${e.message}", e)
        }
}
