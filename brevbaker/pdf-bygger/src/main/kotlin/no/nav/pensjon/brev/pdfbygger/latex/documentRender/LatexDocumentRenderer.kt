package no.nav.pensjon.brev.pdfbygger.latex.documentRender

import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.api.toLanguage
import no.nav.pensjon.brev.pdfbygger.latex.LatexAppendable
import no.nav.pensjon.brev.pdfbygger.latex.LatexDocument
import no.nav.pensjon.brev.pdfbygger.latex.clean
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.render.pensjonLatexSettings
import no.nav.pensjon.brevbaker.api.model.AttachmentTitle
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.PDFTittel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private const val DOCUMENT_PRODUCER = "brevbaker / pdf-bygger med LaTeX"

internal object LatexDocumentRenderer {

    internal fun render(pdfRequest: PDFRequest): LatexDocument = render(
        letter = pdfRequest.letterMarkup.clean(),
        attachments = pdfRequest.attachments,
        language = pdfRequest.language.toLanguage(),
        brevtype = pdfRequest.brevtype,
        pdfVedlegg = pdfRequest.pdfVedlegg,
    )

    private fun render(
        letter: LetterMarkup,
        attachments: List<LetterMarkup.Attachment>,
        language: Language,
        brevtype: LetterMetadata.Brevtype,
        pdfVedlegg: List<PDFTittel>,
    ): LatexDocument =
        LatexDocument().apply {
            newLatexFile("params.tex") {
                appendMasterTemplateParameters(letter, attachments + pdfVedlegg, brevtype, language)
            }
            newLatexFile("letter.xmpdata") { appendXmpData(letter, language) }
            newLatexFile("letter.tex") { renderLetterTemplate(letter, attachments) }
            attachments.forEachIndexed { id, attachment ->
                newLatexFile("attachment_${id}.tex") { renderAttachment(attachment) }
            }
        }

    private fun LatexAppendable.renderLetterTemplate(letter: LetterMarkup, attachments: List<LetterMarkup.Attachment>) {
        appendln("""\documentclass{pensjonsbrev_v4}""", escape = false)
        appendCmd("begin", "document")
        appendCmd("firstpage")
        appendCmd("tittel", letter.title.renderToString(), escape = false)
        renderBlocks(letter.blocks)
        appendCmd("closing")
        attachments.indices.forEach { id ->
            appendCmd("input", "attachment_$id", escape = false)
        }
        appendCmd("end", "document")
    }

    private fun LatexAppendable.appendMasterTemplateParameters(
        letter: LetterMarkup,
        attachments: List<AttachmentTitle>,
        brevtype: LetterMetadata.Brevtype,
        language: Language,
    ) {
        // TODO: Følgende tekster finnes også i LetterMarkup: LanguageSetting.Closing.greeting
        pensjonLatexSettings.writeLanguageSettings(language) { settingName, settingValue ->
            appendNewCmd("felt$settingName") { append(settingValue) } }

        appendln("\\def\\pdfcreationdate{\\string ${pdfCreationTime()}}", escape = false)

        vedlegg(attachments)
        sakspart(letter.sakspart, language)
        signatur(letter.signatur, brevtype)
    }

    private fun LatexAppendable.appendXmpData(letter: LetterMarkup, language: Language) {
        appendCmd("Title", letter.title.xmpDataTitle(), escape = false) // allerede escapet i renderTexts
        appendCmd("Language", language.locale().toLanguageTag())
        appendCmd("Publisher", letter.signatur.navAvsenderEnhet)
        appendCmd("Date", letter.sakspart.dokumentDato.format(DateTimeFormatter.ISO_LOCAL_DATE))
        appendCmd("Producer", DOCUMENT_PRODUCER)
        appendCmd("Creator", DOCUMENT_PRODUCER)
    }

    private fun pdfCreationTime(): String {
        val now = ZonedDateTime.now()
        val formattedTime = now.format(DateTimeFormatter.ofPattern("YYYYMMddHHmmssxxx"))
        return "D:${formattedTime.replace(":", "'")}'"
    }

    // Custom render to only accept text
    internal fun List<Text>.xmpDataTitle(): String = this.joinToString { it.text }
}