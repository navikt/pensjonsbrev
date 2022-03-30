package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.outlineTestTemplate
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TemplateListTest {
    @Test
    fun `list can be created with default values`() {
        val doc = outlineTestTemplate<Unit> {
            list {
                item {
                    text(Language.Bokmal to "Test")
                }
            }
        }

        val expected = outlineTestLetter(
            Element.ItemList(
                listOf(
                    Element.ItemList.Item(listOf(newText(Language.Bokmal to "Test")))
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

    @Test
    fun `item conditions are added`() {
        val doc = outlineTestTemplate<Unit> {
            list {
                showIf(true.expr()) {
                    item {
                        text(Language.Bokmal to "Test")
                    }
                }
            }
        }

        val expected = outlineTestLetter(
            Element.ItemList(
                listOf(
                    Element.Conditional(
                        true.expr(),
                        listOf(
                            Element.ItemList.Item(
                                listOf(newText(Language.Bokmal to "Test"))
                            )
                        ), emptyList()
                    )
                )
            )
        )

        Assertions.assertEquals(doc, expected)
    }

}