@file:Suppress("DEPRECATION")

package no.nav.pensjon.brev.pdfbygger

import kotlinx.coroutines.runBlocking
import no.nav.brev.brevbaker.document.dsl.document
import no.nav.brev.brevbaker.document.dsl.documentMottaker
import no.nav.brev.brevbaker.document.dsl.documentPDFRequest
import no.nav.brev.brevbaker.document.dsl.documentSaksinformasjon
import no.nav.brev.brevbaker.document.dsl.documentSaksnummer
import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.dsl.cell
import no.nav.brev.brevbaker.markup.dsl.column
import no.nav.brev.brevbaker.markup.dsl.header
import no.nav.brev.brevbaker.markup.dsl.item
import no.nav.brev.brevbaker.markup.dsl.itemList
import no.nav.brev.brevbaker.markup.dsl.numberedList
import no.nav.brev.brevbaker.markup.dsl.paragraph
import no.nav.brev.brevbaker.markup.dsl.row
import no.nav.brev.brevbaker.markup.dsl.table
import no.nav.brev.brevbaker.markup.dsl.title2
import no.nav.brev.brevbaker.markup.dsl.title3
import no.nav.brev.brevbaker.markup.outline.Block.Table.ColumnAlignment
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.nio.file.Path

@Tag(TestTags.INTEGRATION_TEST)
class RenderPDFDocumentVisualITest {

    private val pdfCompileService = PdfByggerTestService()

    @Test
    fun `document med alle elementer`() {
        val request = documentPDFRequest(
            document(
                tittel = "Dokument med alle elementer",
                gjelderNavn = PdfByggerTestData.gjelderNavn,
                gjelderPersonidentifikator = PdfByggerTestData.gjelderPersonidentifikator,
                saksnummer = documentSaksnummer(PdfByggerTestData.saksnummer, visFooter = true),
                dokumentDato = PdfByggerTestData.dokumentDato,
            ) {
                paragraph(
                    "Dette dokumentet viser alle elementene et dokument kan inneholde: logo, " +
                        "saksinformasjon, dokumentdato, tittel og footer."
                )
                title2("Overskrift på nivå 2")
                paragraph("Et avsnitt under overskriften.")
                title3("Overskrift på nivå 3")
                itemList {
                    item("Første punkt i punktlisten")
                    item("Andre punkt i punktlisten")
                }
                numberedList {
                    item("Første steg")
                    item("Andre steg")
                }
                table {
                    header {
                        column("Ytelse")
                        column("Beløp", alignment = ColumnAlignment.RIGHT)
                    }
                    row {
                        cell("Alderspensjon")
                        cell("20 000 kr")
                    }
                    row {
                        cell("Uføretrygd")
                        cell("15 000 kr")
                    }
                }
            },
            Markup.Spraak.BOKMAL,
        )

        val pdf = runBlocking { pdfCompileService.producePDFDokument(request) }

        writeTestPDF(
            pdfFileName = "document med alle elementer",
            pdf = pdf.bytes,
            path = Path.of("build/test_visual/pdf"),
        )
    }

    @Test
    fun `document med delvis saksinformasjon`() {
        val request = documentPDFRequest(
            document(
                tittel = "Dokument med delvis saksinformasjon",
                gjelderNavn = PdfByggerTestData.gjelderNavn,
                annenMottakerNavn = "Annen Mottaker",
                dokumentDato = PdfByggerTestData.dokumentDato,
            ) {
                paragraph("Saksinformasjonen viser kun feltene som er sendt med, og footeren har bare sidetall.")
            },
            Markup.Spraak.BOKMAL,
        )

        val pdf = runBlocking { pdfCompileService.producePDFDokument(request) }

        writeTestPDF(
            pdfFileName = "document med delvis saksinformasjon",
            pdf = pdf.bytes,
            path = Path.of("build/test_visual/pdf"),
        )
    }

    @Test
    fun `document med bare dokumentdato`() {
        val request = documentPDFRequest(
            document(
                tittel = "Dokument med bare dokumentdato",
                dokumentDato = PdfByggerTestData.dokumentDato,
            ) {
                paragraph("Dokumentdatoen vises selv om ingen andre saksinformasjonsfelter er sendt med.")
            },
            Markup.Spraak.BOKMAL,
        )

        val pdf = runBlocking { pdfCompileService.producePDFDokument(request) }

        writeTestPDF(
            pdfFileName = "document med bare dokumentdato",
            pdf = pdf.bytes,
            path = Path.of("build/test_visual/pdf"),
        )
    }

    @Test
    fun `document med legacy saksinformasjon`() {
        val request = documentPDFRequest(
            document(
                tittel = "Dokument med legacy saksinformasjon",
                saksinformasjon = documentSaksinformasjon(
                    saksnummer = PdfByggerTestData.saksnummer,
                    visFooter = true,
                    mottaker = documentMottaker(
                        gjelderNavn = PdfByggerTestData.gjelderNavn,
                        gjelderPersonidentifikator = PdfByggerTestData.gjelderPersonidentifikator,
                    ),
                ),
                dokumentDato = PdfByggerTestData.dokumentDato,
            ) {
                paragraph("Skal se ut som dokumenter bestilt med saksinformasjon-strukturen fra produksjon.")
            },
            Markup.Spraak.BOKMAL,
        )

        val pdf = runBlocking { pdfCompileService.producePDFDokument(request) }

        writeTestPDF(
            pdfFileName = "document med legacy saksinformasjon",
            pdf = pdf.bytes,
            path = Path.of("build/test_visual/pdf"),
        )
    }

    @Test
    fun `document med legacy saksinformasjon uten mottaker`() {
        val request = documentPDFRequest(
            document(
                tittel = "Dokument med legacy saksinformasjon uten mottaker",
                saksinformasjon = documentSaksinformasjon(PdfByggerTestData.saksnummer, visFooter = true),
                dokumentDato = PdfByggerTestData.dokumentDato,
            ) {
                paragraph("Saksnummeret vises kun i footeren, og datoen vises alene.")
            },
            Markup.Spraak.BOKMAL,
        )

        val pdf = runBlocking { pdfCompileService.producePDFDokument(request) }

        writeTestPDF(
            pdfFileName = "document med legacy saksinformasjon uten mottaker",
            pdf = pdf.bytes,
            path = Path.of("build/test_visual/pdf"),
        )
    }
}
