package no.nav.pensjon.brev.template.dsl

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isA
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TextOnlyScopeTest {

    @Test
    fun `addAll adds all items and keeps ordering`() {
        val expected = listOf(
            newText(Bokmal to "1"),
            newText(Bokmal to "2"),
            newText(Bokmal to "3"),
            newText(Bokmal to "4"),
        )

        val actual = TextOnlyScope<LangBokmal, Unit>().apply {
            addAll(expected.subList(0, 2))
            addAll(expected.subList(2, 4))
        }

        assertEquals(expected, actual.children)
    }

    @Test
    fun `no-data includePhrase adds phrase elements`() {
        val phrase = TextOnlyPhrase<LangBokmal, Unit> {
            text(Bokmal to "hei")
        }

        val actual = TextOnlyScope<LangBokmal, Unit>().apply { includePhrase(phrase) }

        assertEquals(listOf(newText(Bokmal to "hei")), actual.children)
    }

    @Test
    fun `includePhrase with arg adds elements`() {
        val phrase = TextOnlyPhrase<LangBokmal, String> {
            textExpr(Bokmal to it)
        }

        val actual = TextOnlyScope<LangBokmal, Unit>().apply { includePhrase(phrase, "hei".expr()) }

        assertEquals(listOf(Element.Text.Expression.ByLanguage.create(Bokmal to "hei".expr())), actual.children)
    }

    @Test
    fun `includePhrase can use langcombo with more languages`() {
        val phrase = TextOnlyPhrase<LangBokmalNynorsk, Unit> {
            text(Bokmal to "hei", Nynorsk to "hei")
        }
        // Will not compile if support is broken
        TextOnlyScope<LangBokmal, Unit>().apply { includePhrase(phrase) }
    }

    @Test
    fun `eval adds expression`() {
        val actual = TextOnlyScope<LangBokmal, Unit>().apply { eval("expr".expr()) }

        assertEquals(listOf(Element.Text.Expression<LangBokmal>("expr".expr())), actual.children)
    }

    @Test
    fun `eval-block adds expression`() {
        val actual = TextOnlyScope<LangBokmal, Unit>().apply { eval { "expr".expr() } }

        assertEquals(listOf(Element.Text.Expression<LangBokmal>("expr".expr())), actual.children)
    }

    @Test
    fun `TemplateTextOnlyScope_newline adds newline element`() {
        val element = TextOnlyScope<BokmalLang, SomeDto>().apply {
            newline()
        }.children.first()

        assertThat(element, isA<Element.NewLine<BokmalLang>>())
    }
}