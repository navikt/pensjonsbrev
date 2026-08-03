package no.nav.brev.brevbaker.pdfbygger.dsl

import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.dsl.*
import no.nav.brev.brevbaker.markup.outline.Text
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LetterPDFRequestDslTest {

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
        val request = letterPDFRequest(
            Markup.Spraak.BOKMAL,
            Markup.Brevtype.VEDTAKSBREV,
            minimalLetter(),
        )

        assertEquals(Markup.Spraak.BOKMAL, request.spraak)
        assertEquals(Markup.Brevtype.VEDTAKSBREV, request.brevtype)
        assertTrue(request.attachments.isEmpty())
        assertTrue(request.pdfVedlegg.isEmpty())
    }

    @Test
    fun `preserves order of multiple attachments and pdfVedlegg`() {
        val request = letterPDFRequest(
            Markup.Spraak.BOKMAL,
            Markup.Brevtype.VEDTAKSBREV,
            minimalLetter(),
        ) {
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

        val request = letterPDFRequest(
            Markup.Spraak.ENGLISH,
            Markup.Brevtype.INFORMASJONSBREV,
            minimalLetter(),
        ) {
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
    fun `letterPDFRequest builds and round-trips through json`() {
        val request = letterPDFRequest(
            spraak = Markup.Spraak.BOKMAL,
            brevtype = Markup.Brevtype.VEDTAKSBREV,
            letter = letterMarkup(
                saksinformasjon = saksinformasjon(
                    gjelderNavn = "Ola Nordmann",
                    gjelderPersonidentifikator = "12345678901",
                    saksnummer = "9876543",
                    dokumentDato = LocalDate.of(2026, 7, 9),
                ),
                signatur = signatur(
                    navAvsenderEnhet = "NAV",
                ),
            ) {
                title1("Vedtak")
                outline {
                    paragraph("Innhold")
                }
            },
        ) {
            attachment(inkluderSaksinformasjon = true) {
                title1("Vedlegg 1")
                outline {
                    paragraph("Vedleggsinnhold")
                }
            }
            pdfVedlegg { text("Ekstra PDF-vedlegg") }
        }

        assertEquals(Markup.Spraak.BOKMAL, request.spraak)
        assertEquals(Markup.Brevtype.VEDTAKSBREV, request.brevtype)
        assertEquals("Vedtak", (request.letterMarkup.title1.single() as Text.Literal).text)
        assertEquals(1, request.attachments.size)
        assertEquals("Ekstra PDF-vedlegg", (request.pdfVedlegg.single().title1.single() as Text.Literal).text)
    }

    @Test
    fun `letterPDFRequest accepts pre-built components`() {
        val letter = letterMarkup(
            saksinformasjon = saksinformasjon(
                gjelderNavn = "Ola Nordmann",
                gjelderPersonidentifikator = "12345678901",
                saksnummer = "9876543",
                dokumentDato = LocalDate.of(2026, 7, 9),
            ),
            signatur = signatur(navAvsenderEnhet = "NAV"),
        ) {
            title1("Orientering")
            outline { paragraph("Innhold") }
        }
        val vedlegg = attachment { title1("Vedlegg"); outline { paragraph("X") } }
        val tittel = pdfTittel { text("Tittel") }

        val request = letterPDFRequest(
            spraak = Markup.Spraak.NYNORSK,
            brevtype = Markup.Brevtype.INFORMASJONSBREV,
            letter = letter,
        ) {
            attachment(vedlegg)
            pdfVedlegg(tittel)
        }

        assertEquals(letter, request.letterMarkup)
        assertEquals(listOf(vedlegg), request.attachments)
        assertEquals(listOf(tittel), request.pdfVedlegg)
    }
}
