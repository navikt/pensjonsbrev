package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.PDFRequestV2
import no.nav.pensjon.brev.api.toLanguage
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.pdfbygger.clean
import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.pensjon.brev.pdfbygger.typst.TypstFileWriter
import no.nav.pensjon.brev.pdfbygger.typst.typstStringEscape
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dateFormatter
import no.nav.pensjon.brev.template.render.documentLanguageSettings
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.PDFTittelV2
import java.time.format.FormatStyle

object TypstDocumentRendererV2 {

    internal fun render(pdfRequest: PDFRequestV2, typstWriter: TypstFileWriter): Unit = render(
        letter = pdfRequest.letterMarkup.clean(),
        attachments = pdfRequest.attachments.clean(),
        language = pdfRequest.language.toLanguage(),
        brevtype = pdfRequest.brevtype,
        pdfVedlegg = pdfRequest.pdfVedlegg,
        typstWriter = typstWriter,
    )

    private fun render(
        letter: LetterMarkupV2,
        attachments: List<LetterMarkupV2.Attachment>,
        language: Language,
        brevtype: LetterMetadata.Brevtype,
        pdfVedlegg: List<PDFTittelV2>,
        typstWriter: TypstFileWriter,
    ): Unit = typstWriter.codeScope {
        appendInputData(letter, attachments, language, brevtype, pdfVedlegg)
        renderLetterTemplate(letter, attachments)
    }

    private fun TypstCodeScope.appendInputData(
        letter: LetterMarkupV2,
        attachments: List<LetterMarkupV2.Attachment>,
        language: Language,
        brevtype: LetterMetadata.Brevtype,
        pdfVedlegg: List<PDFTittelV2>,
    ) {
        // Language settings dictionary
        appendDictionary("languageSettings", documentLanguageSettings.languageSettings(language))

        // Input data dictionary with all letter metadata
        appendDictionary(
            "input",
            mapOf(
                "gjelderNavn" to letter.saksinformasjon.gjelderNavn,
                "gjelderFoedselsnummer" to letter.saksinformasjon.gjelderFoedselsnummer.format(),
                "annenMottakerNavn" to letter.saksinformasjon.annenMottakerNavn,
                "saksnummer" to letter.saksinformasjon.saksnummer.saksnummer,
                "dokumentDato" to letter.saksinformasjon.dokumentDato.format(dateFormatter(language, FormatStyle.LONG)),
                "avsenderEnhet" to letter.signatur.navAvsenderEnhet,
                "signerendeSaksbehandler" to letter.signatur.saksbehandlerSignatur?.saksbehandlerNavn,
                "signerendeAttestant" to letter.signatur.saksbehandlerSignatur?.attesterendeSaksbehandlerNavn?.takeIf { brevtype == LetterMetadata.Brevtype.VEDTAKSBREV },
                "erVedtaksbrev" to (brevtype == LetterMetadata.Brevtype.VEDTAKSBREV),
                "attachments" to buildAttachmentTitleList(attachments, pdfVedlegg),
            )
        )
    }

    /**
     * Build a list of attachment titles for the closing section.
     */
    private fun buildAttachmentTitleList(
        attachments: List<LetterMarkupV2.Attachment>,
        pdfVedlegg: List<PDFTittelV2>
    ): List<String> {
        val attachmentTitles = attachments.map { attachment ->
            attachment.title1.renderToPlainStringV2()
        }
        val pdfVedleggTitles = pdfVedlegg.map { vedlegg ->
            vedlegg.title1.renderToPlainStringV2()
        }
        return attachmentTitles + pdfVedleggTitles
    }

    /**
     * Render the main letter.typ file content.
     *
     * Structure mirrors v1's [no.nav.pensjon.brev.pdfbygger.typst.documentrender.TypstDocumentRenderer],
     * with the letter's own title coming from `title1` (renamed from v1's `title`)
     * and body blocks (Title2/3/4, Paragraph, ItemList, NumberedList, Table) as a
     * flat sibling list.
     */
    private fun TypstCodeScope.renderLetterTemplate(
        letter: LetterMarkupV2,
        attachments: List<LetterMarkupV2.Attachment>
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
        val letterTitle = letter.title1.renderToPlainStringV2()
        appendCodeln("""#show: template.with(""")
        appendCodeln("""  lettertitle: "${letterTitle.typstStringEscape()}",""")
        appendCodeln("""  input: input,""")
        appendCodeln("""  languageSettings: languageSettings,""")
        appendCodeln(""")""")
        appendCodeln()

        // Main content block
        appendCodeln("#{")

        // Render all letter content blocks
        renderBlocksV2(letter.blocks)

        // Closing section
        appendCodeln("  closing(input, languageSettings)")

        // Render attachments
        attachments.forEachIndexed { index, attachment ->
            // Section numbers start at 2 (1 is the main letter)
            renderAttachmentV2(attachment, sectionNumber = index + 2)
        }

        // End marker for PDF processing
        appendCodeln("""  [#metadata("end") <endOfLetter>]""")
        appendCodeln("}")
    }
}
