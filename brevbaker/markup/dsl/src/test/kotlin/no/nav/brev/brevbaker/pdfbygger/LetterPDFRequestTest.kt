package no.nav.brev.brevbaker.pdfbygger

import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.dsl.*
import no.nav.brev.brevbaker.markup.outline.Text
import no.nav.brev.brevbaker.pdfbygger.api.letterPDFRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LetterPDFRequestTest {

    private fun minimalLetter(title: String = "Vedtak"): LetterMarkup = letterMarkup(
        saksinformasjon = saksinformasjon(
            gjelderNavn = "Ola Nordmann",
            gjelderPersonidentifikator = "12345678901",
            saksnummer = "9876543",
            dokumentDato = LocalDate.of(2026, 7, 9),
        ),
        signatur = signatur(navAvsenderEnhet = "NAV"),
    ) {
        title1(title)
        outline { paragraph("Innhold") }
    }

    private fun List<Text>.literalText(): String = (single() as Text.Literal).text

    @Test
    fun `builds request with only a letter and empty vedlegg lists`() {
        val letter = minimalLetter()
        val request = letterPDFRequest(
            letterMarkup = letter,
            spraak = Markup.Spraak.BOKMAL,
            brevtype = Markup.Brevtype.VEDTAKSBREV,
        )

        assertEquals(letter, request.letterMarkup)
        assertEquals(Markup.Spraak.BOKMAL, request.spraak)
        assertEquals(Markup.Brevtype.VEDTAKSBREV, request.brevtype)
        assertTrue(request.attachments.isEmpty())
        assertTrue(request.pdfVedlegg.isEmpty())
    }

    @Test
    fun `preserves order of attachments and pdfVedlegg`() {
        val request = letterPDFRequest(
            letterMarkup = minimalLetter(),
            spraak = Markup.Spraak.ENGLISH,
            brevtype = Markup.Brevtype.INFORMASJONSBREV,
            attachments = listOf(
                attachment { title1("A1"); outline { paragraph("x") } },
                attachment(inkluderSaksinformasjon = true) { title1("A2"); outline { paragraph("y") } },
            ),
            pdfVedlegg = listOf(pdfTittel { text("P1") }, pdfTittel { text("P2") }),
        )

        assertEquals(listOf("A1", "A2"), request.attachments.map { it.title1.literalText() })
        assertTrue(request.attachments[1].inkluderSaksinformasjon)
        assertEquals(listOf("P1", "P2"), request.pdfVedlegg.map { it.title1.literalText() })
    }
}
