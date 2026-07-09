package no.nav.brev.brevbaker.markup.elements

import no.nav.brev.brevbaker.markup.LetterMarkupV2
import no.nav.brev.brevbaker.markup.MarkupJson
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Block.FormText.Size
import no.nav.brev.brevbaker.markup.outline.Block.Table.ColumnAlignment
import no.nav.brev.brevbaker.markup.outline.Text
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MarkupElementsTest {

    @Test
    fun `text elements default ids to zero and honour overrides`() {
        assertEquals(0, literalElement("x").id)
        assertEquals(0, variableElement("y").id)
        assertEquals(0, newLineElement().id)
        assertEquals(42, literalElement("x", id = 42).id)
    }

    @Test
    fun `block elements can be built standalone with default zero ids`() {
        val paragraph = paragraphElement {
            literal("Hei")
            variable("navn")
            newLine()
        }
        assertEquals(0, paragraph.id)
        assertEquals(listOf(Text.Type.LITERAL, Text.Type.VARIABLE, Text.Type.NEW_LINE), paragraph.content.map { it.type })
        assertTrue(paragraph.content.all { it.id == 0 })

        val list = itemListElement {
            item { literal("a") }
            item { literal("b") }
        }
        assertEquals(2, list.items.size)

        val table = tableElement {
            header {
                column(alignment = ColumnAlignment.LEFT) { literal("A") }
                column(alignment = ColumnAlignment.RIGHT, span = 2) { literal("B") }
            }
            row {
                cell { literal("a1") }
                cell { literal("b1") }
            }
        }
        assertEquals(2, table.header.colSpec.size)
        assertEquals(1, table.rows.size)
        assertEquals(2, table.rows.single().cells.size)

        val formText = formTextElement(Size.LONG) { literal("Skriv") }
        assertEquals(Size.LONG, formText.size)

        val formChoice = formChoiceElement {
            prompt { literal("Velg") }
            choice { literal("Ja") }
            choice { literal("Nei") }
        }
        assertEquals(2, formChoice.choices.size)
    }

    @Test
    fun `nested element builders can also be used directly`() {
        val cell = cellElement(id = 7) { literal("x") }
        assertEquals(7, cell.id)
        val row = rowElement { cell { literal("x") } }
        assertEquals(1, row.cells.size)
        val header = headerElement { column { literal("h") } }
        assertEquals(1, header.colSpec.size)
        val item = itemElement { literal("i") }
        assertEquals(0, item.id)
        val choice = choiceElement { literal("c") }
        assertEquals(0, choice.id)
    }

    @Test
    fun `composite letter elements can be built standalone`() {
        val letter = letterMarkupElement(
            saksinformasjon = saksinformasjonElement(
                gjelderNavn = "Ola",
                gjelderFoedselsnummer = "12345678901",
                saksnummer = "9876543",
            ),
            signatur = signaturElement(
                hilsenTekst = "Hilsen",
                navAvsenderEnhet = "NAV",
                saksbehandlerSignatur = saksbehandlerSignaturElement("Saks Behandler"),
            ),
            title1 = pdfTittelElement(listOf(literalElement("Tittel"))).title1,
            blocks = listOf(paragraphElement { literal("Innhold") }),
        )
        assertEquals("Ola", letter.saksinformasjon.gjelderNavn)
        assertEquals("12345678901", letter.saksinformasjon.gjelderFoedselsnummer.value)
        assertEquals(LetterMarkupV2.VERSION, letter.version)
        assertEquals(1, letter.blocks.size)

        val withUsage = letterMarkupWithDataUsageElement(
            markup = letter,
            letterDataUsage = setOf(dataUsagePropertyElement("Dto", "felt")),
        )
        assertEquals(1, withUsage.letterDataUsage.size)
    }

    @Test
    fun `standalone element round-trips through json`() {
        val block: Block = paragraphElement(id = 5) { literal("Hei", id = 1) }
        val json = MarkupJson.encodeToString(Block.serializer(), block)
        val decoded = MarkupJson.decodeFromString(Block.serializer(), json)
        assertEquals(block, decoded)
    }
}
