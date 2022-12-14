package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.Fixtures.felles
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.expression.*
import org.junit.jupiter.api.Test

class ForEachViewTest {

    @Test
    fun `ForEachView will render the body for all list items`() {
        val listen = listOf("hei", "ha det bra", "og", "goodbye")
        val actual = outlineTestTemplate<Unit> {
            val myList = listen.expr()

            forEach(myList) { x ->
                eval(x)
            }
        }

        Letter(actual, Unit, Language.Bokmal, felles).assertRenderedLetterContainsAllOf(*listen.toTypedArray())
    }

    @Test
    fun `ForEachView works for nested forEach`() {
        val listen = listOf(listOf("hei", "hello", "bonjour"), listOf("ha det bra", "goodbye", "au revoir"))
        val actual = outlineTestTemplate<Unit> {
            val myList = listen.expr()

            forEach(myList) { subList ->
                forEach(subList) {
                    eval(it)
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

            forEach(myList) { x ->
                eval(argument.select(selector) + x)
            }
        }

        Letter(actual, Argument("Tja:"), Language.Bokmal, felles).assertRenderedLetterContainsAllOf(*listen.map { "Tja:$it" }.toTypedArray())
    }

}