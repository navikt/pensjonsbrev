package no.nav.brev.brevbaker.markup

import no.nav.brev.brevbaker.markup.dsl.letterMarkup
import no.nav.brev.brevbaker.markup.dsl.letterMarkupWithVariables
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Block.FormText.Size
import no.nav.brev.brevbaker.markup.outline.Block.Table.ColumnAlignment
import no.nav.brev.brevbaker.markup.outline.Text
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LetterMarkupDslTest {

    private fun fullLetter(): LetterMarkupV2 = letterMarkupWithVariables {
        title { literal("Vedtak om uføretrygd") }
        saksinformasjon(
            gjelderNavn = "Ola Nordmann",
            gjelderFoedselsnummer = "12345678901",
            saksnummer = "9876543",
            dokumentDato = LocalDate.of(2026, 7, 9),
            annenMottakerNavn = "Kari Nordmann",
        )
        outline {
            title2 { literal("Innledning") }
            paragraph {
                literal("Du får ")
                variable("uføretrygd")
                literal(".")
                newLine()
            }
            itemList {
                item { literal("Punkt 1") }
                item { literal("Punkt 2") }
            }
            numberedList {
                item { literal("Steg 1") }
            }
            table {
                header {
                    column(ColumnAlignment.LEFT) { literal("Kolonne A") }
                    column(ColumnAlignment.RIGHT, span = 2) { literal("Kolonne B") }
                }
                row {
                    cell { literal("A1") }
                    cell { literal("B1") }
                }
            }
            formText(Size.LONG) { literal("Skriv her") }
            formChoice {
                prompt { literal("Velg") }
                choice { literal("Ja") }
                choice { literal("Nei") }
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
            title { literal("Tittel") }
            saksinformasjon(
                gjelderNavn = "Ola Nordmann",
                gjelderFoedselsnummer = "12345678901",
                saksnummer = "9876543",
                dokumentDato = LocalDate.of(2026, 7, 9),
            )
            outline {
                paragraph {
                    literal("Kun litteral tekst.")
                    newLine()
                }
            }
            signatur(
                hilsenTekst = "Med vennlig hilsen",
                navAvsenderEnhet = "NAV",
                saksbehandlerNavn = "Sak S.Behandler",
            )
        }

        val paragraph = letter.blocks.filterIsInstance<Block.Paragraph>().single()
        assertEquals(listOf(Text.Type.LITERAL, Text.Type.NEW_LINE), paragraph.content.map { it.type })
    }
}
