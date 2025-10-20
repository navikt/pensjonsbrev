package no.nav.pensjon.brev.template.dsl

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.ContentOrControlStructure.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TextOnlyScopeTest {

    @Test
    fun `no-data includePhrase adds phrase elements`() {
        val phrase = object : TextOnlyPhrase<LangBokmal>() {
            override fun TextOnlyScope<LangBokmal, Unit>.template() = text(bokmal { +"hei" })
        }

        val actual = TextOnlyScope<LangBokmal, Unit>().apply { includePhrase(phrase) }

        assertEquals(listOf(newText(Bokmal to "hei")), actual.elements)
    }

    data class TextPhraseWithArg(val str: Expression<String>): TextOnlyPhrase<LangBokmal>() {
        override fun TextOnlyScope<LangBokmal, Unit>.template() = text(bokmal { +str })
    }

    @Test
    fun `includePhrase with arg adds elements`() {
        val actual = TextOnlyScope<LangBokmal, Unit>().apply { includePhrase(TextPhraseWithArg("hei".expr())) }

        assertEquals(listOf(Content(Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(Bokmal to "hei".expr()))), actual.elements)
    }

    object TestPlainTextOnlyPhrase : PlainTextOnlyPhrase<LangBokmal>() {
        override fun PlainTextOnlyScope<LangBokmal, Unit>.template() = text(bokmal { + "hei" })
    }

    @Test
    fun `includePhrase adds elements from plain text only phrase`() {
        val actual = TextOnlyScope<LangBokmal, Unit>().apply { includePhrase(TestPlainTextOnlyPhrase) }

        assertEquals(listOf(Content(Element.OutlineContent.ParagraphContent.Text.Literal.create(Bokmal to "hei"))), actual.elements)
    }

    @Test
    fun `includePhrase can use langcombo with more languages`() {
        val phrase = object : TextOnlyPhrase<LangBokmalNynorsk>() {
            override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() = text(bokmal { +"hei" }, nynorsk { + "hei" })
        }
        // Will not compile if support is broken
        TextOnlyScope<LangBokmal, Unit>().apply { includePhrase(phrase) }
    }

    @Test
    fun `eval adds expression`() {
        val actual = TextOnlyScope<LangBokmal, Unit>().apply { eval("expr".expr()) }

        assertEquals(listOf(Content(Element.OutlineContent.ParagraphContent.Text.Expression<LangBokmal>("expr".expr()))), actual.elements)
    }

    @Test
    fun `TemplateTextOnlyScope_newline adds newline element`() {
        val element = TextOnlyScope<LangBokmal, SomeDto>().apply {
            newline()
        }.elements.first()

        when (element) {
            is Content -> assertThat(element.content, isA<Element.OutlineContent.ParagraphContent.Text.NewLine<LangBokmal>>())
            else -> fail("Element should be Content")
        }
    }
}

private data class SomeDto(val name: String, val pensjonInnvilget: Boolean, val kortNavn: String? = null)