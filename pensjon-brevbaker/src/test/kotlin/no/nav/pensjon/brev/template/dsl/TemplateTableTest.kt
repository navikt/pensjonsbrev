package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.outlineTestLetter
import no.nav.pensjon.brev.template.outlineTestTemplate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class TemplateTableTest {
    @Test
    fun `table can be created with default values`() {


        val doc = outlineTestTemplate {
            table {
                title {
                    text(Language.Bokmal to "tittel")
                }
                columnHeaderRow {
                    cell {
                        text(Language.Bokmal to "header")
                    }
                }
                row {
                    cell {
                        text(Language.Bokmal to "joda")
                    }
                }
            }
        }

        val expected = outlineTestLetter(
            Element.Table(
                title = listOf(newText(Language.Bokmal to "tittel")),
                columnHeaders = listOf(
                    Element.Table.Row(
                        listOf(
                            Element.Table.Cell(
                                listOf(newText(Language.Bokmal to "header")), 1
                            )
                        )
                    )
                ),
                rows = listOf(
                    Element.Table.Row(
                        listOf(
                            Element.Table.Cell(
                                listOf(newText(Language.Bokmal to "joda")), 1
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
        assertThrows(IllegalArgumentException::class.java) {
            outlineTestTemplate {
                table {
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
        assertThrows(IllegalArgumentException::class.java) {
            outlineTestTemplate {
                table {
                    title {
                        Language.Bokmal to "Hello world!"
                    }
                    row {

                    }
                    row {

                    }
                }
            }
        }
    }

    @Test
    fun `table creation fails when title is missing`() {
        assertThrows(IllegalArgumentException::class.java) {
            outlineTestTemplate {
                table {
                    row {
                        Language.Bokmal to "Hello world!"
                    }
                }
            }
        }
    }

    @Test
    fun `table creation fails when title is empty`() {
        assertThrows(IllegalArgumentException::class.java) {
            outlineTestTemplate {
                table {
                    title {

                    }
                    row {
                        Language.Bokmal to "Hello world!"
                    }
                }
            }
        }
    }

    @Test
    fun `showif adds rows with predicates`() {
        val doc = outlineTestTemplate {
            table {
                title { text(Language.Bokmal to "tittel") }
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
                                listOf(newText(Language.Bokmal to "hei")), 1
                            )
                        ), true.expr()
                    ),
                    Element.Table.Row(
                        listOf(
                            Element.Table.Cell(
                                listOf(newText(Language.Bokmal to "heihå")), 1
                            )
                        ), true.expr()
                    )
                ),
                title = listOf(newText(Language.Bokmal to "tittel")),
                columnHeaders = emptyList()
            )
        )
        assertEquals(expected, doc)
    }


    @Test
    fun `showif adds column headers with predicates`() {
        val doc = outlineTestTemplate {
            table {
                title { text(Language.Bokmal to "tittel") }
                row {
                    cell {
                        text(
                            Language.Bokmal to "Dette er en rad",
                        )
                    }
                }
                showIf(true.expr()) {
                    columnHeaderRow {
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
                columnHeaders = listOf(
                    Element.Table.Row(
                        listOf(
                            Element.Table.Cell(
                                listOf(newText(Language.Bokmal to "heihå")), 1
                            )
                        ), true.expr()
                    )
                ),
                title = listOf(newText(Language.Bokmal to "tittel")),
                rows = listOf(
                    Element.Table.Row(
                        listOf(
                            Element.Table.Cell(
                                listOf(newText(Language.Bokmal to "Dette er en rad")), 1
                            )
                        )
                    )
                )
            )
        )
        assertEquals(expected, doc)
    }
}