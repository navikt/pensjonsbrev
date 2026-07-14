package no.nav.brev.brevbaker.markup

import no.nav.brev.brevbaker.markup.dsl.*
import no.nav.brev.brevbaker.markup.outline.Text
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

class LetterPDFRequestDslTest {

    private fun minimalLetter(title: String = "Vedtak"): LetterMarkup = letterMarkup {
        saksinformasjon(
            gjelderNavn = "Ola Nordmann",
            gjelderFoedselsnummer = "12345678901",
            saksnummer = "9876543",
            dokumentDato = LocalDate.of(2026, 7, 9),
        )
        title1(title)
        outline { paragraph("Innhold") }
        signatur(hilsenTekst = "Med vennlig hilsen", navAvsenderEnhet = "NAV")
    }

    private fun List<Text>.literalText(): String = (single() as Text.Literal).text

    @Test
    fun `builds request with only a letter and empty vedlegg lists`() {
        val request = letterPDFRequest(LanguageCode.BOKMAL, Brevtype.VEDTAKSBREV) {
            letter(minimalLetter())
        }

        assertEquals(LanguageCode.BOKMAL, request.language)
        assertEquals(Brevtype.VEDTAKSBREV, request.brevtype)
        assertTrue(request.attachments.isEmpty())
        assertTrue(request.pdfVedlegg.isEmpty())
    }

    @Test
    fun `throws when no letter is set`() {
        val exception = assertThrows<IllegalArgumentException> {
            letterPDFRequest(LanguageCode.BOKMAL, Brevtype.VEDTAKSBREV) { }
        }

        assertEquals("LetterPDFRequest must have a letter", exception.message)
    }

    @Test
    fun `last letter call wins`() {
        val request = letterPDFRequest(LanguageCode.BOKMAL, Brevtype.VEDTAKSBREV) {
            letter(minimalLetter("Første"))
            letter(minimalLetter("Andre"))
        }

        assertEquals("Andre", request.letterMarkup.title1.literalText())
    }

    @Test
    fun `preserves order of multiple attachments and pdfVedlegg`() {
        val request = letterPDFRequest(LanguageCode.BOKMAL, Brevtype.VEDTAKSBREV) {
            letter(minimalLetter())
            attachment { title1("A1"); outline { paragraph("x") } }
            attachment { title1("A2"); outline { paragraph("y") } }
            pdfVedlegg { text("P1") }
            pdfVedlegg { text("P2") }
        }

        assertEquals(listOf("A1", "A2"), request.attachments.map { it.title1.literalText() })
        assertEquals(listOf("P1", "P2"), request.pdfVedlegg.map { it.title1.literalText() })
    }

    @Test
    fun `mixes pre-built and dsl-built components`() {
        val prebuiltAttachment = attachment { title1("Ferdig vedlegg"); outline { paragraph("z") } }
        val prebuiltTittel = pdfTittel { text("Ferdig tittel") }

        val request = letterPDFRequest(LanguageCode.ENGLISH, Brevtype.INFORMASJONSBREV) {
            letter(minimalLetter())
            attachment(prebuiltAttachment)
            attachment { title1("DSL vedlegg"); outline { paragraph("q") } }
            pdfVedlegg(prebuiltTittel)
            pdfVedlegg { text("DSL tittel") }
        }

        assertEquals(2, request.attachments.size)
        assertEquals(prebuiltAttachment, request.attachments.first())
        assertEquals("DSL vedlegg", request.attachments[1].title1.literalText())
        assertEquals(2, request.pdfVedlegg.size)
        assertEquals(prebuiltTittel, request.pdfVedlegg.first())
        assertEquals("DSL tittel", request.pdfVedlegg[1].title1.literalText())
    }

    @Test
    fun `round-trips through json for every language and brevtype`() {
        for (language in LanguageCode.entries) {
            for (brevtype in Brevtype.entries) {
                val request = letterPDFRequest(language, brevtype) {
                    letter(minimalLetter())
                    attachment { title1("Vedlegg"); outline { paragraph("x") } }
                    pdfVedlegg { text("Tittel") }
                }

                val decoded = decodeLetterPDFRequest(request.toJson())

                assertEquals(request, decoded)
                assertEquals(language, decoded.language)
                assertEquals(brevtype, decoded.brevtype)
            }
        }
    }
}
