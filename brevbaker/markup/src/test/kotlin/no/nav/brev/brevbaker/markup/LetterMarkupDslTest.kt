package no.nav.brev.brevbaker.markup

import kotlinx.serialization.json.Json
import no.nav.brev.brevbaker.markup.dsl.*
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Block.FormText.Size
import no.nav.brev.brevbaker.markup.outline.Block.Table.ColumnAlignment
import no.nav.brev.brevbaker.markup.outline.Text
import no.nav.brev.brevbaker.markup.outline.Text.FontType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LetterMarkupDslTest {

    @Test
    fun `base letterMarkup builds without variables`() {
        val letter = letterMarkup(
            saksinformasjon = saksinformasjon(
                gjelderNavn = "Ola Nordmann",
                gjelderPersonidentifikator = "12345678901",
                saksnummer = "9876543",
                dokumentDato = LocalDate.of(2026, 7, 9),
            ),
            signatur = signatur(
                navAvsenderEnhet = "NAV",
                saksbehandlerNavn = "Sak S.Behandler",
            ),
        ) {
            title1("Tittel")
            title1 { text("Tittel fra builder") }
            outline {
                title2("Title 2")
                title2 { text("Title 2 builder") }
                title3("Title 3")
                title3 { text("Title 3 builder") }
                title4("Title 4")
                title4 { text("Title 4 builder") }

                paragraph("Paragraph literal", fontType = FontType.BOLD)
                paragraph {
                    text("Kun litteral tekst.")
                    newLine()
                    text("Ny linje.")
                }

                itemList {
                    item("Punkt 1", fontType = FontType.BOLD)
                    item { text("Punkt 2 fra builder") }
                }
                numberedList {
                    item("Steg 1")
                    item { text("Steg 2 fra builder") }
                }

                table {
                    header {
                        column("Kolonne 1")
                        column(alignment = ColumnAlignment.RIGHT) { text("Kolonne 2 builder") }
                    }

                    row {
                        cell("A1", fontType = FontType.BOLD)
                        cell { text("B1") }
                    }
                }

                formText("Ledetekst", Size.SHORT, fontType = FontType.BOLD)
                formText(Size.LONG) { text("Ledetekst fra builder") }
                formChoice(vspace = false) {
                    prompt("Velg")
                    choice("Ja", fontType = FontType.BOLD)
                    choice { text("Nei fra builder") }
                }
            }
        }

        val blockClasses = letter.blocks.map { it::class }
        assertTrue(
            blockClasses.containsAll(
                listOf(
                    Block.Title2::class,
                    Block.Title3::class,
                    Block.Title4::class,
                    Block.Paragraph::class,
                    Block.ItemList::class,
                    Block.NumberedList::class,
                    Block.Table::class,
                    Block.FormText::class,
                    Block.FormChoice::class,
                )
            )
        )
    }

    @Test
    fun `pdfTittel builds standalone title`() {
        val tittel = pdfTittel { text("Vedtak om uføretrygd") }

        assertEquals(1, tittel.title1.size)
        assertEquals("Vedtak om uføretrygd", (tittel.title1.single() as Text.Literal).text)
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

        val decoded = Json.decodeFromString(LetterPDFRequest.serializer(), Json.encodeToString(LetterPDFRequest.serializer(), request))
        assertEquals(request, decoded)
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

    @Test
    fun `attachment builds standalone attachment`() {
        val vedlegg = attachment(inkluderSaksinformasjon = true) {
            title1("Vedlegg 1")
            outline {
                paragraph("Innhold")
            }
        }

        assertEquals(true, vedlegg.inkluderSaksinformasjon)
        assertEquals("Vedlegg 1", (vedlegg.title1.single() as Text.Literal).text)
        val paragraph = vedlegg.blocks.single() as Block.Paragraph
        assertEquals("Innhold", (paragraph.content.single() as Text.Literal).text)
    }
}
