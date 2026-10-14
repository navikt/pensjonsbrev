package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.brev.brevbaker.document.Document
import no.nav.brev.brevbaker.document.DocumentPDFRequest
import no.nav.brev.brevbaker.document.clean
import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.pensjon.brev.pdfbygger.typst.TypstFileWriter
import no.nav.pensjon.brev.pdfbygger.typst.typstStringEscape
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import java.time.format.FormatStyle

object TypstDocumentRenderer {

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
                "gjelderNavn" to document.saksinformasjon?.mottaker?.gjelderNavn,
                "gjelderFoedselsnummer" to document.saksinformasjon?.mottaker?.gjelderPersonidentifikator?.format(),
                "annenMottakerNavn" to document.saksinformasjon?.mottaker?.annenMottakerNavn,
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
        appendCodeln("""  showCaseDetails: ${(document.saksinformasjon?.mottaker != null).typst()},""")
        appendCodeln("""  showDocumentDate: ${(document.dokumentDato != null).typst()},""")
        appendCodeln("""  showFooter: ${(document.saksinformasjon?.visFooter == true).typst()},""")
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
