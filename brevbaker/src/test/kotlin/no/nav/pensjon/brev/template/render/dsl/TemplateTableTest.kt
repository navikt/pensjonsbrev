package no.nav.pensjon.brev.template.render.dsl

import com.natpryce.hamkrest.assertion.assertThat
import no.nav.brev.brevbaker.outlineTestTemplate
import no.nav.pensjon.brev.template.ContentOrControlStructureImpl.ConditionalImpl
import no.nav.pensjon.brev.template.ContentOrControlStructureImpl.ContentImpl
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.ElementImpl
import no.nav.pensjon.brev.template.InvalidTableDeclarationException
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.render.Fixtures
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brev.template.render.hasBlocks
import no.nav.pensjon.brev.template.render.outlineTestLetter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class TemplateTableTest {
    @Test
    fun `table can be created with default values`() {

        val doc = outlineTestTemplate<Unit> {
            paragraph {
                table(header = {
                    column {
                        text(Bokmal to "header")
                    }
                }) {
                    row {
                        cell {
                            text(Bokmal to "joda")
                        }
                    }
                }
            }
        }

        val colSpec = listOf(
            ElementImpl.OutlineContentImpl.ParagraphContentImpl.TableImpl.ColumnSpecImpl(
                ElementImpl.OutlineContentImpl.ParagraphContentImpl.TableImpl.CellImpl(
                    listOf(newText(Bokmal to "header"))
                ), Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT
            )
        )
        val expected = outlineTestLetter(
            ContentImpl(
                ElementImpl.OutlineContentImpl.ParagraphImpl(
                    listOf(
                        ContentImpl(
                            ElementImpl.OutlineContentImpl.ParagraphContentImpl.TableImpl(
                                header = ElementImpl.OutlineContentImpl.ParagraphContentImpl.TableImpl.HeaderImpl(colSpec),
                                rows = listOf(
                                    ContentImpl(
                                        ElementImpl.OutlineContentImpl.ParagraphContentImpl.TableImpl.RowImpl(
                                            listOf(
                                                ElementImpl.OutlineContentImpl.ParagraphContentImpl.TableImpl.CellImpl(
                                                    listOf(newText(Bokmal to "joda"))
                                                )
                                            ), colSpec
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        assertEquals(doc, expected)
    }

    @Test
    fun `table creation fails when rows have uneven amount of cells`() {
        assertThrows(InvalidTableDeclarationException::class.java) {
            outlineTestTemplate<Unit> {
                paragraph {
                    table(header = {
                        column {
                            text(Bokmal to "header")
                        }
                    }) {
                        row {
                            cell {
                                text(Bokmal to "en enkel celle")
                            }
                        }
                        row {
                            cell {
                                text(Bokmal to "en Henkel celle")
                            }
                            cell {
                                text(Bokmal to "en celle for mye")
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `table creation fails when rows are missing cells`() {
        assertThrows(InvalidTableDeclarationException::class.java) {
            outlineTestTemplate<Unit> {
                paragraph {
                    table(header = {
                        column {
                            text(Bokmal to "header")
                        }
                    }) {
                        row {

                        }
                    }
                }
            }
        }
    }


    @Test
    fun `showif adds rows with predicates`() {
        val doc = outlineTestTemplate<Unit> {
            paragraph {
                table(header = {
                    column {
                        text(Bokmal to "header")
                    }
                }) {
                    showIf(true.expr()) {
                        row {
                            cell {
                                text(
                                    Bokmal to "hei",
                                )
                            }
                        }
                        row {
                            cell {
                                text(
                                    Bokmal to "heihå",
                                )
                            }
                        }
                    }
                }
            }
        }
        val colSpec = listOf(
            ElementImpl.OutlineContentImpl.ParagraphContentImpl.TableImpl.ColumnSpecImpl(
                ElementImpl.OutlineContentImpl.ParagraphContentImpl.TableImpl.CellImpl(
                    listOf(newText(Bokmal to "header"))
                ), Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT
            )
        )
        val expected = outlineTestLetter(
            ContentImpl(
                ElementImpl.OutlineContentImpl.ParagraphImpl(
                    listOf(
                        ContentImpl(
                            ElementImpl.OutlineContentImpl.ParagraphContentImpl.TableImpl(
                                rows = listOf(
                                    ConditionalImpl(
                                        true.expr(),
                                        listOf(
                                            ElementImpl.OutlineContentImpl.ParagraphContentImpl.TableImpl.RowImpl(
                                                listOf(
                                                    ElementImpl.OutlineContentImpl.ParagraphContentImpl.TableImpl.CellImpl(
                                                        listOf(newText(Bokmal to "hei"))
                                                    )
                                                ),
                                                colSpec = colSpec
                                            ),
                                            ElementImpl.OutlineContentImpl.ParagraphContentImpl.TableImpl.RowImpl(
                                                listOf(
                                                    ElementImpl.OutlineContentImpl.ParagraphContentImpl.TableImpl.CellImpl(
                                                        listOf(newText(Bokmal to "heihå"))
                                                    )
                                                ), colSpec = colSpec
                                            )
                                        ).map { ContentImpl(it) }, emptyList()
                                    )
                                ),
                                header = ElementImpl.OutlineContentImpl.ParagraphContentImpl.TableImpl.HeaderImpl(colSpec)
                            )
                        )
                    )
                )
            )
        )
        assertEquals(expected, doc)
    }

    @Test
    fun `table is not rendered when all the rows are filtered out`() {
        val doc = outlineTestTemplate<Unit> {
            title1 { text(Bokmal to "THIS TEXT SHOULD RENDER") }
            paragraph {
                table(
                    header = {
                        column {
                            text(
                                Bokmal to "This text should not render1",
                            )
                        }
                    }
                ) {
                    showIf(false.expr()) {
                        row {
                            cell {
                                text(
                                    Bokmal to "This text should not render2",
                                )
                            }
                        }
                    }
                }
            }
        }

        val actual = Letter2Markup.render(LetterImpl(doc, Unit, Bokmal, Fixtures.felles)).letterMarkup
        assertThat(
            actual,
            hasBlocks {
                title1 { literal("THIS TEXT SHOULD RENDER") }
                paragraph {  }
            }
        )
    }

    @Test
    fun `all table elements are rendered`() {
        val doc = outlineTestTemplate<Unit> {
            paragraph {
                table(
                    header = {
                        column {
                            text(
                                Bokmal to "This text should render 1",
                            )
                        }
                    }
                ) {
                    showIf(true.expr()) {
                        row {
                            cell {
                                text(
                                    Bokmal to "This text should render 2",
                                )
                            }
                        }
                    }
                }
            }
        }

        assertThat(
            Letter2Markup.render(LetterImpl(doc, Unit, Bokmal, Fixtures.felles)).letterMarkup,
            hasBlocks {
                paragraph {
                    table {
                        header { column { literal("This text should render 1") } }
                        row { cell { literal("This text should render 2") } }
                    }
                }
            }
        )
    }
}