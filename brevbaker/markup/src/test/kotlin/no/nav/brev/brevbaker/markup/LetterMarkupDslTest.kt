package no.nav.brev.brevbaker.markup

import no.nav.brev.brevbaker.markup.dsl.HeaderBuilder
import no.nav.brev.brevbaker.markup.dsl.column
import no.nav.brev.brevbaker.markup.dsl.letterMarkup
import no.nav.brev.brevbaker.markup.dsl.lettermarkupExtended
import no.nav.brev.brevbaker.markup.dsl.prompt
import no.nav.brev.brevbaker.markup.dsl.title
import no.nav.brev.brevbaker.markup.dsl.title2
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Block.FormText.Size
import no.nav.brev.brevbaker.markup.outline.Block.Table.ColumnAlignment
import no.nav.brev.brevbaker.markup.outline.Text
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LetterMarkupDslTest {

    private fun fullLetter(): LetterMarkupV2 = lettermarkupExtended {
        title { literal("Vedtak om uføretrygd") }
        saksinformasjon(
            gjelderNavn = "Ola Nordmann",
            gjelderFoedselsnummer = "12345678901",
            saksnummer = "9876543",
            dokumentDato = LocalDate.of(2026, 7, 9),
            annenMottakerNavn = "Kari Nordmann",
        )
        outline {
            title2 { text("Innledning") }
            paragraph {
                text("Du får ")
                variable("uføretrygd")
                text(".")
                newLine()
            }
            itemList {
                item { text("Punkt 1") }
                item { text("Punkt 2") }
            }
            numberedList {
                item { text("Steg 1") }
            }
            table {
                header {
                    column(ColumnAlignment.LEFT) {
                        literal("Kolonne ")
                        variable("A")
                    }
                    column(ColumnAlignment.RIGHT, span = 2) { literal("Kolonne B") }
                }
                row {
                    cell { text("A1") }
                    cell { text("B1") }
                }
            }
            formText(Size.LONG) { text("Skriv her") }
            formChoice {
                prompt { literal("Velg") }
                choice { text("Ja") }
                choice { text("Nei") }
            }
        }
        signatur(
            hilsenTekst = "Med vennlig hilsen",
            navAvsenderEnhet = "NAV Familie- og pensjonsytelser",
            saksbehandlerNavn = "Sak S.Behandler",
            attesterendeSaksbehandlerNavn = "Att Esterer",
        )
    }

    @Test
    fun `dsl builds a valid letter with all block and text types`() {
        val letter = fullLetter()

        assertEquals(2, letter.version)
        assertEquals("Ola Nordmann", letter.saksinformasjon.gjelderNavn)
        assertEquals("12345678901", letter.saksinformasjon.gjelderFoedselsnummer.value)
        assertEquals("9876543", letter.saksinformasjon.saksnummer.saksnummer)

        val types = letter.blocks.map { it.type }
        assertTrue(types.containsAll(listOf(
            Block.Type.TITLE2,
            Block.Type.PARAGRAPH,
            Block.Type.ITEM_LIST,
            Block.Type.NUMBERED_LIST,
            Block.Type.TABLE,
            Block.Type.FORM_TEXT,
            Block.Type.FORM_CHOICE,
        )))

        val paragraph = letter.blocks.filterIsInstance<Block.Paragraph>().single()
        val textTypes = paragraph.content.map { it.type }
        assertTrue(textTypes.containsAll(listOf(Text.Type.LITERAL, Text.Type.VARIABLE, Text.Type.NEW_LINE)))
    }

    @Test
    fun `dsl assigns unique ids`() {
        val letter = fullLetter()
        val ids = mutableListOf<Int>()
        letter.title1.forEach { ids.add(it.id) }
        letter.blocks.forEach { block ->
            ids.add(block.id)
            when (block) {
                is Block.Title2 -> block.content.forEach { ids.add(it.id) }
                is Block.Paragraph -> block.content.forEach { ids.add(it.id) }
                else -> {}
            }
        }
        assertEquals(ids.size, ids.toSet().size, "ids should be unique")
    }

    @Test
    fun `letter round-trips through json`() {
        val letter = fullLetter()
        val json = MarkupJson.encodeToString(LetterMarkupV2.serializer(), letter)
        val decoded = MarkupJson.decodeFromString(LetterMarkupV2.serializer(), json)
        assertEquals(letter, decoded)
    }

    @Test
    fun `base letterMarkup builds without variables`() {
        val letter = letterMarkup {
            title("Tittel")
            saksinformasjon(
                gjelderNavn = "Ola Nordmann",
                gjelderFoedselsnummer = "12345678901",
                saksnummer = "9876543",
                dokumentDato = LocalDate.of(2026, 7, 9),
            )
            outline {
                title2("")
                paragraph {
                    text("Kun litteral tekst.")
                }
                table{
                    header {
                        column("Bla!")
                        column("Bla!")
                        HeaderBuilder.plainText(HeaderB)
                        column("Bla!")
                    }

                    row {
                        cell {
                            text("mø")
                        }
                        cell {
                            text("mø")
                        }
                        cell {
                            text("mø")
                        }
                        cell("mø")
                    }
                }
            }
            signatur(
                hilsenTekst = "Med vennlig hilsen",
                navAvsenderEnhet = "NAV",
                saksbehandlerNavn = "Sak S.Behandler",
            )
        }

        val paragraph = letter.blocks.filterIsInstance<Block.Paragraph>().single()
        assertEquals(listOf(Text.Type.LITERAL), paragraph.content.map { it.type })
    }
}
