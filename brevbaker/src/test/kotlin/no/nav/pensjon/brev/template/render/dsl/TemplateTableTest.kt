package no.nav.pensjon.brev.template.render.dsl

import com.natpryce.hamkrest.assertion.assertThat
import no.nav.brev.brevbaker.outlineTestTemplate
import no.nav.pensjon.brev.template.ContentOrControlStructure.Conditional
import no.nav.pensjon.brev.template.ContentOrControlStructure.Content
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.InvalidTableDeclarationException
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Letter
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
            Element.OutlineContent.ParagraphContent.Table.ColumnSpec(
                Element.OutlineContent.ParagraphContent.Table.Cell(
                    listOf(newText(Bokmal to "header"))
                ), Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT
            )
        )
        val expected = outlineTestLetter(
            Content(
                Element.OutlineContent.Paragraph(
                    listOf(
                        Content(
                            Element.OutlineContent.ParagraphContent.Table(
                                header = Element.OutlineContent.ParagraphContent.Table.Header(colSpec),
                                rows = listOf(
                                    Content(
                                        Element.OutlineContent.ParagraphContent.Table.Row(
                                            listOf(
                                                Element.OutlineContent.ParagraphContent.Table.Cell(
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
            Element.OutlineContent.ParagraphContent.Table.ColumnSpec(
                Element.OutlineContent.ParagraphContent.Table.Cell(
                    listOf(newText(Bokmal to "header"))
                ), Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT
            )
        )
        val expected = outlineTestLetter(
            Content(
                Element.OutlineContent.Paragraph(
                    listOf(
                        Content(
                            Element.OutlineContent.ParagraphContent.Table(
                                rows = listOf(
                                    Conditional(
                                        true.expr(),
                                        listOf(
                                            Element.OutlineContent.ParagraphContent.Table.Row(
                                                listOf(
                                                    Element.OutlineContent.ParagraphContent.Table.Cell(
                                                        listOf(newText(Bokmal to "hei"))
                                                    )
                                                ),
                                                colSpec = colSpec
                                            ),
                                            Element.OutlineContent.ParagraphContent.Table.Row(
                                                listOf(
                                                    Element.OutlineContent.ParagraphContent.Table.Cell(
                                                        listOf(newText(Bokmal to "heihå"))
                                                    )
                                                ), colSpec = colSpec
                                            )
                                        ).map { Content(it) }, emptyList()
                                    )
                                ),
                                header = Element.OutlineContent.ParagraphContent.Table.Header(colSpec)
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

        val actual = Letter2Markup.render(Letter(doc, Unit, Bokmal, Fixtures.felles)).letterMarkup
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
            Letter2Markup.render(Letter(doc, Unit, Bokmal, Fixtures.felles)).letterMarkup,
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