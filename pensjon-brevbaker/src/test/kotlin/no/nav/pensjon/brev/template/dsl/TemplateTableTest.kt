package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.base.PensjonLatex
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TemplateTableTest {
    @Test
    fun `table can be created with default values`() {
        val doc = createTemplate(
            name = "test",
            base = PensjonLatex,
            letterDataType = Unit::class,
            lang = languages(Language.Bokmal),
            title = bokmalTittel,
            letterMetadata = testLetterMetadata,
        ) {
            outline {
                table {
                    row {
                        cell {
                            text(Language.Bokmal to "joda")
                        }
                    }
                }
            }
        }

        val expected = LetterTemplate(
            name = "test",
            title = bokmalTittel,
            base = PensjonLatex,
            letterDataType = Unit::class,
            language = languages(Language.Bokmal),
            letterMetadata = testLetterMetadata,
            outline = listOf(
                Element.Table(
                    listOf(
                        Element.Table.Row(
                            listOf(
                                Element.Table.Cell(
                                    listOf(newText(Language.Bokmal to "joda")), 1
                                )
                            ), Element.Table.RowColour.WHITE
                        )
                    )
                )
            )
        )

        assertEquals(doc, expected)
    }

    @Test
    fun `table creation fails when rows have uneven amount of cells`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            createTemplate(
                name = "test",
                base = PensjonLatex,
                letterDataType = Unit::class,
                lang = languages(Language.Bokmal),
                title = bokmalTittel,
                letterMetadata = testLetterMetadata,
            ) {
                outline {
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
    }

    @Test
    fun `table creation fails when rows are missing cells`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            createTemplate(
                name = "test",
                base = PensjonLatex,
                letterDataType = Unit::class,
                lang = languages(Language.Bokmal),
                title = bokmalTittel,
                letterMetadata = testLetterMetadata,
            ) {
                outline {
                    table {
                        row {

                        }
                        row {

                        }
                    }
                }
            }
        }
    }

}