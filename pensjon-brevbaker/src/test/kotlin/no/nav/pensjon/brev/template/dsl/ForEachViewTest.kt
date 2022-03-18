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
        val listen = listOf("greetings" to listOf("hei", "hello", "bonjour"), "goodbyes" to listOf("ha det bra", "goodbye", "au revoir"))
        val actual = outlineTestTemplate<Unit> {
            val myList = listen.expr()

            forEach(myList) { subList ->
                val outer = subList.map { it.first }
                forEach(subList.map { it.second }) {
                    eval(outer + it)
                }
            }
        }
        val allWords = listen.map { it.first } + listen.flatMap { it.second }
        Letter(actual, Unit, Language.Bokmal, felles).assertRenderedLetterContainsAllOf(*allWords.toTypedArray())
    }

    @Test
    fun `ForEachView render works with letter argument`() {
        val listen = listOf("hei", "ha det bra", "og", "goodbye")
        val actual = outlineTestTemplate<String> {
            val myList = listen.expr()

            forEach(myList) { x ->
                eval(argument() + x)
            }
        }

        Letter(actual, "Tja:", Language.Bokmal, felles).assertRenderedLetterContainsAllOf(*listen.map { "Tja:$it" }.toTypedArray())
    }

}