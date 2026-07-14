package no.nav.brev.brevbaker.markup.elements

import no.nav.brev.brevbaker.markup.decodeBlock
import no.nav.brev.brevbaker.markup.toJson
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Block.FormText.Size
import no.nav.brev.brevbaker.markup.outline.Block.Table.ColumnAlignment
import no.nav.brev.brevbaker.markup.outline.Text
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MarkupElementsTest {

    @Test
    fun `returns the concrete type of the single constructed element`() {
        val text: Text.Literal = markupElement { literal("x") }
        assertEquals("x", text.text)

        val variable: Text.Variable = markupElement { variable("v") }
        assertEquals("v", variable.text)

        val paragraph: Block.Paragraph = markupElement { paragraph(literal("a")) }
        assertEquals(1, paragraph.content.size)

        val table: Block.Table = markupElement {
            table(header(columnSpec(cell(literal("A")))), row(cell(literal("a1"))))
        }
        assertEquals(1, table.rows.size)
    }

    @Test
    fun `ids default to zero and honour overrides`() {
        assertEquals(0, markupElement { literal("x") }.id)
        assertEquals(0, markupElement { paragraph(literal("x")) }.id)
        assertEquals(7, markupElement { literal("x", id = 7) }.id)
        assertEquals(9, markupElement { paragraph(literal("x"), id = 9) }.id)
    }

    @Test
    fun `flat composition builds nested structures`() {
        val paragraph = markupElement {
            paragraph(literal("Hei"), variable("navn"), newLine())
        }
        assertEquals(
            listOf(Text.Type.LITERAL, Text.Type.VARIABLE, Text.Type.NEW_LINE),
            paragraph.content.map { it.type },
        )

        val list = markupElement {
            itemList(item(literal("a")), item(literal("b")))
        }
        assertEquals(2, list.items.size)

        val table = markupElement {
            table(
                header(
                    columnSpec(cell(literal("A")), alignment = ColumnAlignment.LEFT),
                    columnSpec(cell(literal("B")), alignment = ColumnAlignment.RIGHT, span = 2),
                ),
                row(cell(literal("a1")), cell(literal("b1"))),
            )
        }
        assertEquals(2, table.header.colSpec.size)
        assertEquals(2, table.rows.single().cells.size)

        val formText = markupElement { formText(Size.LONG, literal("Skriv")) }
        assertEquals(Size.LONG, formText.size)

        val formChoice = markupElement {
            formChoice(prompt = listOf(literal("Velg")), choice(literal("Ja")), choice(literal("Nei")))
        }
        assertEquals(2, formChoice.choices.size)
    }

    @Test
    fun `standalone element round-trips through json`() {
        val block: Block = markupElement { paragraph(literal("Hei", id = 1), id = 5) }
        val json = block.toJson()
        val decoded = decodeBlock(json)
        assertEquals(block, decoded)
    }
}
