package no.nav.pensjon.brev.template

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LetterValidationTest {

    @Nested
    inner class ElementTest {

        @Test
        fun `tom ItemList skal feile`() {
            assertThrows<InvalidListDeclarationException> {
                Element.OutlineContent.ParagraphContent.ItemList<LangNynorsk>(listOf())
            }
        }

        @Test
        fun `tabell skal feile for 0 rader`() {
            assertThrows<InvalidTableDeclarationException> {
                Element.OutlineContent.ParagraphContent.Table<LangNynorsk>(
                    rows = listOf(),
                    header = Element.OutlineContent.ParagraphContent.Table.Header(listOf()),
                )
            }
        }

        @Test
        fun `element skal feile for 0 kolonner i tabell`() {
            assertThrows<InvalidTableDeclarationException> {
                Element.OutlineContent.ParagraphContent.Table.Row<LangNynorsk>(
                    cells = listOf(), colSpec = listOf()
                )
            }
        }

        @Test
        fun `header skal feile for 0 header-celler i tabell`() {
            assertThrows<InvalidTableDeclarationException> {
                Element.OutlineContent.ParagraphContent.Table.Header<LangNynorsk>(colSpec = listOf())
            }
        }

        @Test
        fun `skal feile hvis ulikt antall kolonner`() {
            val columnSpec = Element.OutlineContent.ParagraphContent.Table.ColumnSpec<LangNynorsk>(
                headerContent = Element.OutlineContent.ParagraphContent.Table.Cell(listOf()),
                alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT,
                columnSpan = 1
            )
            assertThrows<InvalidTableDeclarationException> {
                Element.OutlineContent.ParagraphContent.Table(
                    rows = listOf(
                        ContentOrControlStructure.Content(
                            Element.OutlineContent.ParagraphContent.Table.Row(
                                cells = listOf(Element.OutlineContent.ParagraphContent.Table.Cell(listOf())),
                                colSpec = listOf(columnSpec)
                            )
                        )
                    ),
                    header = Element.OutlineContent.ParagraphContent.Table.Header(listOf(columnSpec, columnSpec)),
                )
            }
        }
    }

}