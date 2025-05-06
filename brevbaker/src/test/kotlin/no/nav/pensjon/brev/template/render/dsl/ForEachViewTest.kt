package no.nav.pensjon.brev.template.render.dsl

import com.natpryce.hamkrest.assertion.assertThat
import no.nav.brev.brevbaker.outlineTestTemplate
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.expression.select
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.render.Fixtures.felles
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brev.template.render.hasBlocks
import org.junit.jupiter.api.Test

class ForEachViewTest {

    @Test
    fun `ForEachView will render the body for all list items`() {
        val listen = listOf("hei", "ha det bra", "og", "goodbye")
        val actual = outlineTestTemplate<Unit> {
            val myList = listen.expr()
            val str = 5.expr().format()

            paragraph {
                forEach(myList) { x ->
                    eval(x)
                }
            }

            repeat(3) {
                title1 { eval(str) }
            }
        }

        assertThat(
            Letter2Markup.render(LetterImpl(actual, Unit, Language.Bokmal, felles)).letterMarkup,
            hasBlocks {
                paragraph {
                    listen.forEach { variable(it) }
                }
                repeat(3) {
                    title1 { variable("5") }
                }
            },
        )
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

        assertThat(
            Letter2Markup.render(LetterImpl(actual, Unit, Language.Bokmal, felles)).letterMarkup,
            hasBlocks {
                paragraph {
                    listen.forEach { nestedList ->
                        nestedList.forEach { str ->
                            variable(str)
                        }
                    }
                }
            }
        )
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

        val render = Letter2Markup.render(LetterImpl(actual, Argument("Tja:"), Language.Bokmal, felles))
        assertThat(
            render.letterMarkup,
            hasBlocks {
                paragraph {
                    listen.forEach { str ->
                        variable("Tja:")
                        variable(str)
                    }
                }
            }
        )
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

        assertThat(
            Letter2Markup.render(LetterImpl(template, Unit, Language.Bokmal, felles)).letterMarkup,
            hasBlocks {
                paragraph {
                    list.forEach { outer ->
                        list.forEach { inner ->
                            variable(outer)
                            literal(",")
                            variable(inner)
                            literal(";")
                        }
                    }
                }
            }
        )
    }

    data class ListArgument(val liste: List<String>)

    @TemplateModelHelpers
    object Helpers : HasModel<ListArgument>
}