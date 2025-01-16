package no.nav.pensjon.brev.template.dsl

import com.natpryce.hamkrest.assertion.assertThat
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.outlineTestLetter
import no.nav.pensjon.brev.outlineTestTemplate
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.ContentOrControlStructure.Content
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brev.template.render.hasBlocks
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TemplateListTest {
    @Test
    fun `list can be created with default values`() {
        val doc = outlineTestTemplate<Unit> {
            paragraph {
                list {
                    item {
                        text(Bokmal to "Test")
                    }
                }
            }
        }

        val expected = outlineTestLetter(
            Content(
                Element.OutlineContent.Paragraph(
                    listOf(
                        Content(
                            Element.OutlineContent.ParagraphContent.ItemList(
                                listOf(
                                    Content(
                                        Element.OutlineContent.ParagraphContent.ItemList.Item(listOf(newText(Bokmal to "Test")))
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
                            text(Bokmal to "Test")
                        }
                    }
                }
            }
        }

        val expected = outlineTestLetter(
            Content(
                Element.OutlineContent.Paragraph(
                    listOf(
                        Content(
                            Element.OutlineContent.ParagraphContent.ItemList(
                                listOf(
                                    ContentOrControlStructure.Conditional(
                                        true.expr(),
                                        listOf(
                                            Content(
                                                Element.OutlineContent.ParagraphContent.ItemList.Item(
                                                    listOf(newText(Bokmal to "Test"))
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

    @Test
    fun `list is not rendered when items are filtered out`() {
        val doc = outlineTestTemplate<Unit> {
            title1 { text(Bokmal to "this text should render") }
            paragraph {
                list {
                    showIf(false.expr()) {
                        item { text(Bokmal to "This text should not render") }
                    }
                }
            }
        }

        assertThat(
            Letter2Markup.render(Letter(doc, Unit, Bokmal, Fixtures.felles)).letterMarkup,
            hasBlocks {
                title1 { literal("this text should render") }
                paragraph { }
            }
        )
    }

}