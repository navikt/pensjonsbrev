package no.nav.pensjon.brev.template.render.dsl

import no.nav.pensjon.brev.template.ContentOrControlStructure
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.InvalidListDeclarationException
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TemplateListTest {
    @Test
    fun `list can be created with default values`() {
        val doc = outlineTestTemplate<Unit> {
            paragraph {
                list {
                    item {
                        text(Language.Bokmal to "Test")
                    }
                }
            }
        }

        val expected = outlineTestLetter(
            ContentOrControlStructure.Content(
                Element.OutlineContent.Paragraph(
                    listOf(
                        ContentOrControlStructure.Content(
                            Element.OutlineContent.ParagraphContent.ItemList(
                                listOf(
                                    ContentOrControlStructure.Content(
                                        Element.OutlineContent.ParagraphContent.ItemList.Item(listOf(newText(Language.Bokmal to "Test")))
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
    fun `list creation fails when list has no item definitions`() {
        Assertions.assertThrows(InvalidListDeclarationException::class.java) {
            val nullStr: String? = null
            outlineTestTemplate<Unit> {
                paragraph {
                    list {
                        showIf(true.expr()) {

                        }
                        ifNotNull(nullStr.expr()) {

                        }
                        forEach(listOf<String>().expr()) {
                            showIf(true.expr()) {

                            }
                            ifNotNull(nullStr.expr()) {

                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `item conditions are added`() {
        val doc = outlineTestTemplate<Unit> {
            paragraph {
                list {
                    showIf(true.expr()) {
                        item {
                            text(Language.Bokmal to "Test")
                        }
                    }
                }
            }
        }

        val expected = outlineTestLetter(
            ContentOrControlStructure.Content(
                Element.OutlineContent.Paragraph(
                    listOf(
                        ContentOrControlStructure.Content(
                            Element.OutlineContent.ParagraphContent.ItemList(
                                listOf(
                                    ContentOrControlStructure.Conditional(
                                        true.expr(),
                                        listOf(
                                            ContentOrControlStructure.Content(
                                                Element.OutlineContent.ParagraphContent.ItemList.Item(
                                                    listOf(newText(Language.Bokmal to "Test"))
                                                )
                                            )
                                        ), emptyList()
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

}