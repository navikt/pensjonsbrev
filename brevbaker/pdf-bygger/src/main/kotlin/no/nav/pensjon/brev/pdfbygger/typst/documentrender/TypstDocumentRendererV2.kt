package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.LetterPDFRequest
import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.PDFTittel
import no.nav.brev.brevbaker.markup.clean
import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.pensjon.brev.pdfbygger.typst.TypstFileWriter
import no.nav.pensjon.brev.pdfbygger.typst.typstStringEscape
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import java.time.format.FormatStyle

object TypstDocumentRendererV2 {

    internal fun render(pdfRequest: LetterPDFRequest, typstWriter: TypstFileWriter): Unit = render(
        letter = pdfRequest.letterMarkup.clean(),
        attachments = pdfRequest.attachments.clean(),
        language = pdfRequest.spraak.toLanguageCode(),
        brevtype = pdfRequest.brevtype,
        pdfVedlegg = pdfRequest.pdfVedlegg,
        typstWriter = typstWriter,
    )

    private fun render(
        letter: LetterMarkup,
        attachments: List<Attachment>,
        language: LanguageCode,
        brevtype: Markup.Brevtype,
        pdfVedlegg: List<PDFTittel>,
        typstWriter: TypstFileWriter,
    ): Unit = typstWriter.codeScope {
        appendInputData(letter, attachments, language, brevtype, pdfVedlegg)
        renderLetterTemplate(letter, attachments)
    }

    private fun TypstCodeScope.appendInputData(
        letter: LetterMarkup,
        attachments: List<Attachment>,
        language: LanguageCode,
        brevtype: Markup.Brevtype,
        pdfVedlegg: List<PDFTittel>,
    ) {
        // Language settings dictionary
        appendDictionary("languageSettings", DocumentLanguageSettings(language).asMap())

        // Input data dictionary with all letter metadata
        appendDictionary(
            "input",
            mapOf(
                "gjelderNavn" to letter.saksinformasjon.gjelderNavn,
                "gjelderFoedselsnummer" to letter.saksinformasjon.gjelderPersonidentifikator.format(),
                "annenMottakerNavn" to letter.saksinformasjon.annenMottakerNavn,
                "saksnummer" to letter.saksinformasjon.saksnummer.value,
                "dokumentDato" to letter.saksinformasjon.dokumentDato.format(pdfDateFormatter(language, FormatStyle.LONG)),
                "avsenderEnhet" to letter.signatur.navAvsenderEnhet,
                "signerendeSaksbehandler" to letter.signatur.saksbehandlerSignatur?.saksbehandlerNavn,
                "signerendeAttestant" to letter.signatur.saksbehandlerSignatur?.attesterendeSaksbehandlerNavn?.takeIf { brevtype == Markup.Brevtype.VEDTAKSBREV },
                "erVedtaksbrev" to (brevtype == Markup.Brevtype.VEDTAKSBREV),
                "attachments" to buildAttachmentTitleList(attachments, pdfVedlegg),
            )
        )
    }

    /**
     * Build a list of attachment titles for the closing section.
     */
    private fun buildAttachmentTitleList(
        attachments: List<Attachment>,
        pdfVedlegg: List<PDFTittel>
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
        letter: LetterMarkup,
        attachments: List<Attachment>
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

private fun Markup.Spraak.toLanguageCode(): LanguageCode =
    when (this) {
        Markup.Spraak.BOKMAL -> LanguageCode.BOKMAL
        Markup.Spraak.NYNORSK -> LanguageCode.NYNORSK
        Markup.Spraak.ENGLISH -> LanguageCode.ENGLISH
    }

private val personidentRegex = Regex("([0-9]{6})([0-9]{5})")

private fun Markup.Personidentifikator.format(): String =
    personidentRegex.replace(value, "${'$'}1 ${'$'}2")
