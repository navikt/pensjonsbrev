package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.brev.brevbaker.PDFRequest
import no.nav.brev.brevbaker.PdfLanguageSettings
import no.nav.brev.brevbaker.formatPdf
import no.nav.brev.brevbaker.pdfDateFormatter
import no.nav.pensjon.brev.pdfbygger.clean
import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.pensjon.brev.pdfbygger.typst.TypstFileWriter
import no.nav.pensjon.brev.pdfbygger.typst.typstStringEscape
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.PDFTittel
import java.time.format.FormatStyle

object TypstDocumentRenderer {

    internal fun render(pdfRequest: PDFRequest, typstWriter: TypstFileWriter): Unit = render(
        letter = pdfRequest.letterMarkup.clean(),
        attachments = pdfRequest.attachments.clean(),
        language = pdfRequest.language,
        brevtype = pdfRequest.brevtype,
        pdfVedlegg = pdfRequest.pdfVedlegg,
        typstWriter = typstWriter,
    )

    private fun render(
        letter: LetterMarkup,
        attachments: List<LetterMarkup.Attachment>,
        language: LanguageCode,
        brevtype: LetterMetadata.Brevtype,
        pdfVedlegg: List<PDFTittel>,
        typstWriter: TypstFileWriter,
    ): Unit = typstWriter.codeScope {
        appendInputData(letter, attachments, language, brevtype, pdfVedlegg)
        renderLetterTemplate(letter, attachments)
    }

    private fun TypstCodeScope.appendInputData(
        letter: LetterMarkup,
        attachments: List<LetterMarkup.Attachment>,
        language: LanguageCode,
        brevtype: LetterMetadata.Brevtype,
        pdfVedlegg: List<PDFTittel>,
    ) {
        // Language settings dictionary
        appendDictionary("languageSettings", PdfLanguageSettings.forLanguage(language))

        // Input data dictionary with all letter metadata
        appendDictionary(
            "input",
            mapOf(
                "gjelderNavn" to letter.sakspart.gjelderNavn,
                "gjelderFoedselsnummer" to letter.sakspart.gjelderFoedselsnummer.formatPdf(),
                "annenMottakerNavn" to letter.sakspart.annenMottakerNavn,
                "saksnummer" to letter.sakspart.saksnummer,
                "dokumentDato" to letter.sakspart.dokumentDato.format(pdfDateFormatter(language, FormatStyle.LONG)),
                "avsenderEnhet" to letter.signatur.navAvsenderEnhet,
                "signerendeSaksbehandler" to letter.signatur.saksbehandlerNavn,
                "signerendeAttestant" to letter.signatur.attesterendeSaksbehandlerNavn?.takeIf { brevtype == LetterMetadata.Brevtype.VEDTAKSBREV },
                "erVedtaksbrev" to (brevtype == LetterMetadata.Brevtype.VEDTAKSBREV),
                "attachments" to buildAttachmentTitleList(attachments, pdfVedlegg),
            )
        )
    }

    /**
     * Build a list of attachment titles for the closing section.
     */
    private fun buildAttachmentTitleList(
        attachments: List<LetterMarkup.Attachment>,
        pdfVedlegg: List<PDFTittel>
    ): List<String> {
        val attachmentTitles = attachments.map { attachment ->
            attachment.title.renderToPlainString()
        }
        val pdfVedleggTitles = pdfVedlegg.map { vedlegg ->
            vedlegg.title.joinToString("") { it.text }
        }
        return attachmentTitles + pdfVedleggTitles
    }

    /**
     * Render the main letter.typ file content.
     *
     * Structure:
     * - Imports from template files
     * - Template setup with letter title
     * - Content blocks (titles, paragraphs, lists, tables)
     * - Closing section
     * - Attachments
     * - End marker
     */
    private fun TypstCodeScope.renderLetterTemplate(
        letter: LetterMarkup,
        attachments: List<LetterMarkup.Attachment>
    ) {
        // Imports
        appendCodeln("""#import "template.typ": template""")
        appendCodeln("""#import "content/title.typ": title1, title2, title3""")
        appendCodeln("""#import "content/paragraph.typ": paragraph""")
        appendCodeln("""#import "content/list.typ": bulletlist, numberedlist""")
        appendCodeln("""#import "content/table.typ": letter-table""")
        appendCodeln("""#import "content/form.typ": formChoice, formText""")
        appendCodeln("""#import "attachment.typ": startAttachment, endAttachment""")
        appendCodeln("""#import "closing.typ": closing""")

        // Template setup with letter title
        val letterTitle = letter.title.renderToPlainString()
        appendCodeln("""#show: template.with(""")
        appendCodeln("""  lettertitle: "${letterTitle.typstStringEscape()}",""")
        appendCodeln("""  input: input,""")
        appendCodeln("""  languageSettings: languageSettings,""")
        appendCodeln(""")""")
        appendCodeln()

        // Main content block
        appendCodeln("#{")

        // Render all letter content blocks
        renderBlocks(letter.blocks)

        // Closing section
        appendCodeln("  closing(input, languageSettings)")

        // Render attachments
        attachments.forEachIndexed { index, attachment ->
            // Section numbers start at 2 (1 is the main letter)
            renderAttachment(attachment, sectionNumber = index + 2)
        }

        // End marker for PDF processing
        appendCodeln("""  [#metadata("end") <endOfLetter>]""")
        appendCodeln("}")
    }
}