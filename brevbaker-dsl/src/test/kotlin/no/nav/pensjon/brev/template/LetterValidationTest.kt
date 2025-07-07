package no.nav.pensjon.brev.template

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

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
        fun `skal feile for 0 kolonner i tabell`() {
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

    @Nested
    @OptIn(InterneDataklasser::class)
    inner class LetterMarkupTest {
        @Test
        fun `tom ItemList skal feile`() {
            val element = LetterMarkupImpl.ParagraphContentImpl.ItemListImpl(2, listOf())
            assertThrows<InvalidListDeclarationException> {
                lagLetterMarkupImpl(element)
            }
        }

        @Test
        fun `tabell skal feile for 0 rader`() {
            assertThrows<InvalidTableDeclarationException> {
                lagLetterMarkupImpl(
                    LetterMarkupImpl.ParagraphContentImpl.TableImpl(
                        id = Random.nextInt(),
                        rows = listOf(),
                        header = LetterMarkupImpl.ParagraphContentImpl.TableImpl.HeaderImpl(
                            id = Random.nextInt(),
                            colSpec = listOf()
                        )
                    )
                )
            }
        }

        @Test
        fun `skal feile for 0 kolonner i tabell`() {
            assertThrows<InvalidTableDeclarationException> {
                lagLetterMarkupImpl(
                    LetterMarkupImpl.ParagraphContentImpl.TableImpl(
                        id = Random.nextInt(),
                        rows = listOf(
                            LetterMarkupImpl.ParagraphContentImpl.TableImpl.RowImpl(
                                id = Random.nextInt(),
                                cells = listOf()
                            )
                        ),
                        header = LetterMarkupImpl.ParagraphContentImpl.TableImpl.HeaderImpl(
                            id = Random.nextInt(),
                            colSpec = listOf()
                        )
                    )
                )
            }
        }

        @Test
        fun `skal feile hvis ulikt antall kolonner`() {
            assertThrows<InvalidTableDeclarationException> {
                val cell = LetterMarkupImpl.ParagraphContentImpl.TableImpl.CellImpl(
                    id = Random.nextInt(),
                    text = listOf()
                )
                lagLetterMarkupImpl(
                    LetterMarkupImpl.ParagraphContentImpl.TableImpl(
                        id = Random.nextInt(),
                        rows = listOf(
                            LetterMarkupImpl.ParagraphContentImpl.TableImpl.RowImpl(
                                id = Random.nextInt(),
                                cells = listOf(cell, cell)
                            )
                        ),
                        header = LetterMarkupImpl.ParagraphContentImpl.TableImpl.HeaderImpl(
                            id = Random.nextInt(),
                            colSpec = listOf(
                                LetterMarkupImpl.ParagraphContentImpl.TableImpl.ColumnSpecImpl(
                                    id = Random.nextInt(),
                                    headerContent = cell,
                                    alignment = LetterMarkup.ParagraphContent.Table.ColumnAlignment.LEFT,
                                    span = 1
                                )
                            )
                        )
                    )
                )
            }
        }
    }
}

@OptIn(InterneDataklasser::class)
private fun lagLetterMarkupImpl(content: LetterMarkup.ParagraphContent) = LetterMarkupImpl(
    "redigert markup",
    LetterMarkupImpl.SakspartImpl(
        "gjelder bruker",
        "123abc",
        "001",
        "en dato"
    ),
    listOf(
        LetterMarkupImpl.BlockImpl.ParagraphImpl(
            id = Random.nextInt(),
            editable = false,
            content = listOf(content)
        )
    ),
    LetterMarkupImpl.SignaturImpl(
        "hilsen oss",
        "en rolle",
        "Saksbehandlersen",
        null,
        "Akersgata"
    )
)

