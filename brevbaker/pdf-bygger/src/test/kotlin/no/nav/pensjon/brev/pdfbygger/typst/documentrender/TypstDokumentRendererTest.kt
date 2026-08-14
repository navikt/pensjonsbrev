package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.brev.brevbaker.document.DocumentPDFRequest
import no.nav.brev.brevbaker.document.dsl.document
import no.nav.brev.brevbaker.document.dsl.documentPDFRequest
import no.nav.brev.brevbaker.document.dsl.documentMottaker
import no.nav.brev.brevbaker.document.dsl.documentSaksinformasjon
import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.dsl.item
import no.nav.brev.brevbaker.markup.dsl.itemList
import no.nav.brev.brevbaker.markup.dsl.paragraph
import no.nav.brev.brevbaker.markup.dsl.title2
import no.nav.pensjon.brev.pdfbygger.PdfByggerTestData
import no.nav.pensjon.brev.pdfbygger.typst.TypstFileWriter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter

/**
 * Enhetstester for [TypstDocumentRenderer] som inspiserer den genererte Typst-kilden direkte,
 * uten å kreve `typst`-binæren eller en dockerisert kompileringstjeneste.
 */
class TypstDokumentRendererTest {

    private fun render(request: DocumentPDFRequest): String {
        val output = ByteArrayOutputStream()
        OutputStreamWriter(output, Charsets.UTF_8).use { writer ->
            TypstDocumentRenderer.render(request, TypstFileWriter(writer))
        }
        return output.toString(Charsets.UTF_8)
    }

    private val mottaker = documentMottaker(
        gjelderNavn = PdfByggerTestData.gjelderNavn,
        gjelderPersonidentifikator = PdfByggerTestData.gjelderPersonidentifikator,
    )

    private val saksinformasjon = documentSaksinformasjon(
        saksnummer = PdfByggerTestData.saksnummer,
        visFooter = true,
        mottaker = mottaker,
    )

    @Test
    fun `alle elementer synlige`() {
        val typst = render(
            documentPDFRequest(
                document(
                    tittel = "Mitt dokument",
                    saksinformasjon = saksinformasjon,
                    dokumentDato = PdfByggerTestData.dokumentDato,
                ) {
                    title2("Overskrift")
                    paragraph("Et avsnitt.")
                    itemList { item("Punkt 1"); item("Punkt 2") }
                },
                Markup.Spraak.BOKMAL,
            )
        )

        assertThat(typst).contains("""#import "document.typ": documentTemplate""")
        assertThat(typst).contains("""title: "Mitt dokument"""")
        assertThat(typst).contains("showTitle: true")
        assertThat(typst).contains("showLogo: true")
        assertThat(typst).contains("showCaseDetails: true")
        assertThat(typst).contains("showDocumentDate: true")
        assertThat(typst).contains("showFooter: true")
        assertThat(typst).contains(""""${PdfByggerTestData.saksnummer}"""")
        assertThat(typst).contains("1. januar 2020")
        assertThat(typst).contains("""title1[#str("Overskrift")]""")
        assertThat(typst).contains("<endOfDocument>")
    }

    @Test
    fun `alle valgfrie elementer skjult`() {
        val typst = render(
            documentPDFRequest(
                document(tittel = "Bare innhold", visTittel = false, visLogo = false) {
                    paragraph("Et avsnitt.")
                },
                Markup.Spraak.BOKMAL,
            )
        )

        assertThat(typst).contains("showTitle: false")
        assertThat(typst).contains("showLogo: false")
        assertThat(typst).contains("showCaseDetails: false")
        assertThat(typst).contains("showDocumentDate: false")
        assertThat(typst).contains("showFooter: false")
        assertThat(typst).contains("gjelderNavn: none")
        assertThat(typst).contains("saksnummer: none")
        assertThat(typst).contains("dokumentDato: none")
        // Tittelen settes alltid i PDF-metadataene, selv når den ikke vises på siden.
        assertThat(typst).contains("""title: "Bare innhold"""")
    }

    @Test
    fun `dokumentdato uten saksinformasjon`() {
        val typst = render(
            documentPDFRequest(
                document(tittel = "Dokument", dokumentDato = PdfByggerTestData.dokumentDato) {
                    paragraph("Et avsnitt.")
                },
                Markup.Spraak.BOKMAL,
            )
        )

        assertThat(typst).contains("showCaseDetails: false")
        assertThat(typst).contains("showDocumentDate: true")
        assertThat(typst).contains("1. januar 2020")
    }

    @Test
    fun `saksinformasjon uten mottaker gir footer uten saksinformasjonsblokk`() {
        val typst = render(
            documentPDFRequest(
                document(
                    tittel = "Dokument",
                    saksinformasjon = documentSaksinformasjon(
                        saksnummer = PdfByggerTestData.saksnummer,
                        visFooter = true,
                    ),
                ) { paragraph("Et avsnitt.") },
                Markup.Spraak.BOKMAL,
            )
        )

        assertThat(typst).contains("showCaseDetails: false")
        assertThat(typst).contains("showFooter: true")
        assertThat(typst).contains(""""${PdfByggerTestData.saksnummer}"""")
        assertThat(typst).contains("gjelderNavn: none")
    }

    @Test
    fun `nynorsk gir nynorske ledetekster`() {
        val typst = render(
            documentPDFRequest(
                document(tittel = "Dokument", saksinformasjon = saksinformasjon) {
                    paragraph("Eit avsnitt.")
                },
                Markup.Spraak.NYNORSK,
            )
        )

        assertThat(typst).contains("languageSettings")
        assertThat(typst).contains("showCaseDetails: true")
    }
}
