package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.api.toLanguage
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.pdfbygger.clean
import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.pensjon.brev.pdfbygger.typst.typstStringEscape
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dateFormatter
import no.nav.pensjon.brev.template.render.documentLanguageSettings
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.PDFTittel
import java.time.format.FormatStyle

object TypstDocumentRenderer {

    internal fun render(pdfRequest: PDFRequest): (Appendable) -> Unit = render(
        letter = pdfRequest.letterMarkup.clean(),
        attachments = pdfRequest.attachments.clean(),
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
    ): (Appendable) -> Unit = { writer ->
        TypstCodeScope(writer).appendInputData(letter, attachments, language, brevtype, pdfVedlegg)
        TypstCodeScope(writer).renderLetterTemplate(letter, attachments)
    }

    /**
     * Prepend language settings and input data dictionaries into the letter content (stdin).
     */
    private fun TypstCodeScope.appendInputData(
        letter: LetterMarkup,
        attachments: List<LetterMarkup.Attachment>,
        language: Language,
        brevtype: LetterMetadata.Brevtype,
        pdfVedlegg: List<PDFTittel>,
    ) {
        // Language settings dictionary
        appendDictionary("languageSettings", documentLanguageSettings.languageSettings(language))

        // Input data dictionary with all letter metadata
        appendDictionary(
            "input",
            mapOf(
                "gjelderNavn" to letter.sakspart.gjelderNavn,
                "gjelderFoedselsnummer" to letter.sakspart.gjelderFoedselsnummer.format(),
                "annenMottakerNavn" to letter.sakspart.annenMottakerNavn,
                "saksnummer" to letter.sakspart.saksnummer,
                "dokumentDato" to letter.sakspart.dokumentDato.format(dateFormatter(language, FormatStyle.LONG)),
                "avsenderEnhet" to letter.signatur.navAvsenderEnhet,
                "signerendeSaksbehandler" to letter.signatur.saksbehandlerNavn,
                "signerendeAttestant" to letter.signatur.attesterendeSaksbehandlerNavn,
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