package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class TemplateTableTest {
    @Test
    fun `table can be created with default values`() {


        val doc = outlineTestTemplate {
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

        val expected = outlineTestLetter(
            Element.Table(
                header =
                Element.Table.Header(
                    listOf(
                        Element.Table.ColumnSpec(
                            Element.Table.Cell(
                                listOf(newText(Language.Bokmal to "header"))
                            ),
                            Element.Table.ColumnAlignment.LEFT
                        )

                    )
                ),
                rows = listOf(
                    Element.Table.Row(
                        listOf(
                            Element.Table.Cell(
                                listOf(newText(Language.Bokmal to "joda"))
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
            outlineTestTemplate {
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

    @Test
    fun `table creation fails when rows are missing cells`() {
        assertThrows(InvalidTableDeclarationException::class.java) {
            outlineTestTemplate {
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


    @Test
    fun `showif adds rows with predicates`() {
        val doc = outlineTestTemplate {
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
        val expected = outlineTestLetter(
            Element.Table(
                rows = listOf(
                    Element.Table.Row(
                        listOf(
                            Element.Table.Cell(
                                listOf(newText(Language.Bokmal to "hei"))
                            )
                        ), true.expr()
                    ),
                    Element.Table.Row(
                        listOf(
                            Element.Table.Cell(
                                listOf(newText(Language.Bokmal to "heihå"))
                            )
                        ), true.expr()
                    )
                ),
                header =
                Element.Table.Header(
                    listOf(
                        Element.Table.ColumnSpec(
                            Element.Table.Cell(
                                listOf(newText(Language.Bokmal to "header"))
                            ),
                            Element.Table.ColumnAlignment.LEFT
                        )

                    )
                )
            )
        )
        assertEquals(expected, doc)
    }
}