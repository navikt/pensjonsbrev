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

object TypstDocumentRenderer {

    internal fun render(pdfRequest: DocumentPDFRequest, typstWriter: TypstFileWriter): Unit =
        render(pdfRequest.document.clean(), pdfRequest.spraak.toLanguageCode(), typstWriter)

    private fun render(
        document: Document,
        language: LanguageCode,
        typstWriter: TypstFileWriter,
    ): Unit = typstWriter.codeScope {
        val saksinformasjon = document.saksinformasjonForVisning()
        appendInputData(document, saksinformasjon, language)
        renderDocumentTemplate(document, saksinformasjon)
    }

    private fun TypstCodeScope.appendInputData(
        document: Document,
        saksinformasjon: SaksinformasjonForVisning,
        language: LanguageCode,
    ) {
        appendDictionary("languageSettings", DocumentLanguageSettings(language).asMap())

        appendDictionary(
            "input",
            mapOf(
                "gjelderNavn" to saksinformasjon.gjelderNavn,
                "gjelderFoedselsnummer" to saksinformasjon.gjelderPersonidentifikator?.format(),
                "annenMottakerNavn" to saksinformasjon.annenMottakerNavn,
                "saksnummer" to saksinformasjon.saksnummer?.value,
                "footerSaksnummer" to saksinformasjon.footerSaksnummer?.value,
                "dokumentDato" to document.dokumentDato?.format(pdfDateFormatter(language, FormatStyle.LONG)),
            )
        )
    }

    private fun TypstCodeScope.renderDocumentTemplate(document: Document, saksinformasjon: SaksinformasjonForVisning) {
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
        appendCodeln("""  showCaseDetails: ${saksinformasjon.harInnhold().typst()},""")
        appendCodeln("""  showDocumentDate: ${(document.dokumentDato != null).typst()},""")
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

private data class SaksinformasjonForVisning(
    val gjelderNavn: String?,
    val gjelderPersonidentifikator: Markup.Personidentifikator?,
    val annenMottakerNavn: String?,
    val saksnummer: Markup.Saksnummer?,
    val footerSaksnummer: Markup.Saksnummer?,
) {
    fun harInnhold(): Boolean =
        listOf(gjelderNavn, gjelderPersonidentifikator, annenMottakerNavn, saksnummer).any { it != null }
}

@Suppress("DEPRECATION")
private fun Document.saksinformasjonForVisning(): SaksinformasjonForVisning {
    val legacy = saksinformasjon
    return if (legacy != null) {
        val mottaker = legacy.mottaker
        SaksinformasjonForVisning(
            gjelderNavn = mottaker?.gjelderNavn,
            gjelderPersonidentifikator = mottaker?.gjelderPersonidentifikator,
            annenMottakerNavn = mottaker?.annenMottakerNavn,
            saksnummer = legacy.saksnummer.takeIf { mottaker != null },
            footerSaksnummer = legacy.saksnummer.takeIf { legacy.visFooter },
        )
    } else {
        SaksinformasjonForVisning(
            gjelderNavn = gjelderNavn,
            gjelderPersonidentifikator = gjelderPersonidentifikator,
            annenMottakerNavn = annenMottakerNavn,
            saksnummer = saksnummer?.saksnummer,
            footerSaksnummer = saksnummer?.takeIf { it.visFooter }?.saksnummer,
        )
    }
}

private fun Boolean.typst(): String = if (this) "true" else "false"
