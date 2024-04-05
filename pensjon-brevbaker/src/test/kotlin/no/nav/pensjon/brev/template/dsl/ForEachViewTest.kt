package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.Fixtures.felles
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.ForEachViewTestSelectors.ListArgumentSelectors.liste
import no.nav.pensjon.brev.template.dsl.ForEachViewTestSelectors.ListArgumentSelectors.listeSelector
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ForEachViewTest {

    @Test
    fun `ForEachView will render the body for all list items`() {
        val listen = listOf("hei", "ha det bra", "og", "goodbye")
        val actual = outlineTestTemplate<Unit> {
            val myList = listen.expr()

            paragraph {
                forEach(myList) { x ->
                    eval(x)
                }
            }
        }

        Letter(actual, Unit, Language.Bokmal, felles).assertRenderedLetterContainsAllOf(*listen.toTypedArray())
    }

    @Test
    fun `ForEachView works for nested forEach`() {
        val listen = listOf(listOf("hei", "hello", "bonjour"), listOf("ha det bra", "goodbye", "au revoir"))
        val actual = outlineTestTemplate<Unit> {
            val myList = listen.expr()

            paragraph {
                forEach(myList) { subList ->
                    forEach(subList) {
                        eval(it)
                    }
                }
            }
        }
        val allWords = listen.flatten()
        Letter(actual, Unit, Language.Bokmal, felles).assertRenderedLetterContainsAllOf(*allWords.toTypedArray())
    }

    data class Argument(val value: String)

    @Test
    fun `ForEachView render works with letter argument`() {
        val listen = listOf("hei", "ha det bra", "og", "goodbye")
        val selector = object : TemplateModelSelector<Argument, String> {
            override val className = Argument::class.java.name
            override val propertyName = "value"
            override val propertyType = "String"
            override val selector = Argument::value
        }

        val actual = outlineTestTemplate<Argument> {
            val myList = listen.expr()

            paragraph {
                forEach(myList) { x ->
                    eval(argument.select(selector) + x)
                }
            }
        }

        Letter(actual, Argument("Tja:"), Language.Bokmal, felles).assertRenderedLetterContainsAllOf(*listen.map { "Tja:$it" }
            .toTypedArray())
    }

    @Test
    fun `ForEach works with nested loops over the same collection`() {
        val list = listOf("1", "2")
        val template = outlineTestTemplate<EmptyBrevdata> {
            val listExpr = list.expr()
            paragraph {
                forEach(listExpr) { outer ->
                    forEach(listExpr) { inner ->
                        eval(outer + "," + inner + ";")
                    }
                }
            }
        }
        val expected = "1,1;1,2;2,1;2,2;"

        Letter(template, Unit, Language.Bokmal, felles).assertRenderedLetterContainsAllOf(expected)
    }

    data class ListArgument(val liste: List<String>)
    @TemplateModelHelpers
    object Helpers : HasModel<ListArgument>

    @Test
    fun `ForEach uses stableHashCode of items to assign id of Assigned-Expression`() {
        val template = outlineTestTemplate<ListArgument> {
            paragraph {
                forEach(liste) {
                    textExpr(Language.Bokmal to it)
                }
            }
        }

        val itemsExpr = Expression.FromScope.Argument<ListArgument>().select(listeSelector)
        val expectedNext = Expression.FromScope.Assigned<String>(itemsExpr.stableHashCode())

        val expected = ContentOrControlStructure.Content(
            Element.OutlineContent.Paragraph(
                listOf(
                    ContentOrControlStructure.ForEach(
                        itemsExpr,
                        listOf(
                            ContentOrControlStructure.Content(
                                Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(Language.Bokmal to expectedNext)
                            ),
                        ),
                        expectedNext
                    )
                )
            )
        )

        assertEquals(
            expected,
            template.outline.first()
        )
    }

}