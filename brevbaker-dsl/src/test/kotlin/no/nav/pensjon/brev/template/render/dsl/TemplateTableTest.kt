package no.nav.pensjon.brev.template.render.dsl

import no.nav.pensjon.brev.template.ContentOrControlStructure
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.InvalidTableDeclarationException
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TemplateTableTest {
    @Test
    fun `table can be created with default values`() {

        val doc = outlineTestTemplate<Unit> {
            paragraph {
                table(header = {
                    column {
                        text(Language.Bokmal to "header")
                    }
                }) {
                    row {
                        cell {
                            text(Language.Bokmal to "joda")
                        }
                    }
                }
            }
        }

        val colSpec = listOf(
            Element.OutlineContent.ParagraphContent.Table.ColumnSpec(
                Element.OutlineContent.ParagraphContent.Table.Cell(
                    listOf(newText(Language.Bokmal to "header"))
                ), Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT
            )
        )
        val expected = outlineTestLetter(
            ContentOrControlStructure.Content(
                Element.OutlineContent.Paragraph(
                    listOf(
                        ContentOrControlStructure.Content(
                            Element.OutlineContent.ParagraphContent.Table(
                                header = Element.OutlineContent.ParagraphContent.Table.Header(colSpec),
                                rows = listOf(
                                    ContentOrControlStructure.Content(
                                        Element.OutlineContent.ParagraphContent.Table.Row(
                                            listOf(
                                                Element.OutlineContent.ParagraphContent.Table.Cell(
                                                    listOf(newText(Language.Bokmal to "joda"))
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

        Assertions.assertEquals(doc, expected)
    }

    @Test
    fun `table creation fails when rows have uneven amount of cells`() {
        Assertions.assertThrows(InvalidTableDeclarationException::class.java) {
            outlineTestTemplate<Unit> {
                paragraph {
                    table(header = {
                        column {
                            text(Language.Bokmal to "header")
                        }
                    }) {
                        row {
                            cell {
                                text(Language.Bokmal to "en enkel celle")
                            }
                        }
                        row {
                            cell {
                                text(Language.Bokmal to "en Henkel celle")
                            }
                            cell {
                                text(Language.Bokmal to "en celle for mye")
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `table creation fails when rows are missing cells`() {
        Assertions.assertThrows(InvalidTableDeclarationException::class.java) {
            outlineTestTemplate<Unit> {
                paragraph {
                    table(header = {
                        column {
                            text(Language.Bokmal to "header")
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
                        text(Language.Bokmal to "header")
                    }
                }) {
                    showIf(true.expr()) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "hei",
                                )
                            }
                        }
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "heihå",
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
                    listOf(newText(Language.Bokmal to "header"))
                ), Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT
            )
        )
        val expected = outlineTestLetter(
            ContentOrControlStructure.Content(
                Element.OutlineContent.Paragraph(
                    listOf(
                        ContentOrControlStructure.Content(
                            Element.OutlineContent.ParagraphContent.Table(
                                rows = listOf(
                                    ContentOrControlStructure.Conditional(
                                        true.expr(),
                                        listOf(
                                            Element.OutlineContent.ParagraphContent.Table.Row(
                                                listOf(
                                                    Element.OutlineContent.ParagraphContent.Table.Cell(
                                                        listOf(newText(Language.Bokmal to "hei"))
                                                    )
                                                ),
                                                colSpec = colSpec
                                            ),
                                            Element.OutlineContent.ParagraphContent.Table.Row(
                                                listOf(
                                                    Element.OutlineContent.ParagraphContent.Table.Cell(
                                                        listOf(newText(Language.Bokmal to "heihå"))
                                                    )
                                                ), colSpec = colSpec
                                            )
                                        ).map { ContentOrControlStructure.Content(it) }, emptyList()
                                    )
                                ),
                                header = Element.OutlineContent.ParagraphContent.Table.Header(colSpec)
                            )
                        )
                    )
                )
            )
        )
        Assertions.assertEquals(expected, doc)
    }
}