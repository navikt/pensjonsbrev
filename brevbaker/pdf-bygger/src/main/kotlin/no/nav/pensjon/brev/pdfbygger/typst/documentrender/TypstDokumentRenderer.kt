package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.brev.brevbaker.document.Document
import no.nav.brev.brevbaker.document.DocumentPDFRequest
import no.nav.brev.brevbaker.document.clean
import no.nav.brev.brevbaker.markup.Markup
import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.pensjon.brev.pdfbygger.typst.TypstFileWriter
import no.nav.pensjon.brev.pdfbygger.typst.typstStringEscape
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import java.time.format.FormatStyle

/**
 * Renderer for det generiske "document"-konseptet (`/produserDokument`).
 *
 * Merk navnene i denne pakken: [TypstDocumentRenderer] og [TypstDocumentRendererV2] rendrer *brev*
 * ("document" der er PDF-dokumentet), mens denne rendrer datamodellen [Document]. Innholdsblokkene
 * deles med brev via [renderBlocksV2]; det som er eget er Typst-roten (`document.typ`) og hvilke
 * topp-elementer som tas med.
 */
object TypstDokumentRenderer {

    internal fun render(pdfRequest: DocumentPDFRequest, typstWriter: TypstFileWriter): Unit =
        render(pdfRequest.document.clean(), pdfRequest.spraak.toLanguageCode(), typstWriter)

    private fun render(
        document: Document,
        language: LanguageCode,
        typstWriter: TypstFileWriter,
    ): Unit = typstWriter.codeScope {
        appendInputData(document, language)
        renderDocumentTemplate(document)
    }

    private fun TypstCodeScope.appendInputData(document: Document, language: LanguageCode) {
        appendDictionary("languageSettings", DocumentLanguageSettings(language).asMap())

        appendDictionary(
            "input",
            mapOf(
                "gjelderNavn" to document.saksinformasjon?.gjelderNavn,
                "gjelderFoedselsnummer" to document.saksinformasjon?.gjelderPersonidentifikator?.format(),
                "annenMottakerNavn" to document.saksinformasjon?.annenMottakerNavn,
                "saksnummer" to document.saksinformasjon?.saksnummer?.value,
                "dokumentDato" to document.dokumentDato?.format(pdfDateFormatter(language, FormatStyle.LONG)),
            )
        )
    }

    private fun TypstCodeScope.renderDocumentTemplate(document: Document) {
        appendCodeln("""#import "document.typ": documentTemplate""")
        appendCodeln("""#import "content/title.typ": title1, title2, title3""")
        appendCodeln("""#import "content/paragraph.typ": paragraph""")
        appendCodeln("""#import "content/list.typ": bulletlist, numberedlist""")
        appendCodeln("""#import "content/table.typ": letter-table""")
        appendCodeln("""#import "content/form.typ": formChoice, formText""")

        appendCodeln("""#show: documentTemplate.with(""")
        appendCodeln("""  title: "${document.tittel.typstStringEscape()}",""")
        appendCodeln("""  showTitle: ${document.visTittel.typst()},""")
        appendCodeln("""  showLogo: ${document.visLogo.typst()},""")
        appendCodeln("""  showCaseDetails: ${(document.saksinformasjon != null).typst()},""")
        appendCodeln("""  showDocumentDate: ${(document.dokumentDato != null).typst()},""")
        appendCodeln("""  showFooter: ${document.visFooter.typst()},""")
        appendCodeln("""  input: input,""")
        appendCodeln("""  languageSettings: languageSettings,""")
        appendCodeln(""")""")
        appendCodeln()

        appendCodeln("#{")
        renderBlocksV2(document.blocks)
        appendCodeln("""  [#metadata("end") <endOfDocument>]""")
        appendCodeln("}")
    }
}

private fun Boolean.typst(): String = if (this) "true" else "false"

private fun Markup.Spraak.toLanguageCode(): LanguageCode =
    when (this) {
        Markup.Spraak.BOKMAL -> LanguageCode.BOKMAL
        Markup.Spraak.NYNORSK -> LanguageCode.NYNORSK
        Markup.Spraak.ENGLISH -> LanguageCode.ENGLISH
    }

private val personidentRegex = Regex("([0-9]{6})([0-9]{5})")

private fun Markup.Personidentifikator.format(): String =
    personidentRegex.replace(value, "${'$'}1 ${'$'}2")
