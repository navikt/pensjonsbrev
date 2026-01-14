package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.ContentOrControlStructure.Content
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.render.dsl.SomeDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TemplateTextTest {
    @Test
    fun `plain text single language element is created`() {
        val txt = "hei"
        val element = ParagraphOnlyScope<LangBokmal, SomeDto>().apply {
            text(bokmal { +txt })
        }.elements.first()

        val expected = Content(Element.OutlineContent.ParagraphContent.Text.Literal.create(Bokmal to txt))

        assertEquals(expected, element)
    }

    @Test
    fun `plain text with two languages is created`() {
        val txt = "hei"
        val element = ParagraphOnlyScope<LangBokmalNynorsk, SomeDto>().apply {
            text(
                bokmal { +txt },
                nynorsk { +txt },
            )
        }.elements.first()

        val expected = Content(
            Element.OutlineContent.ParagraphContent.Text.Literal.create(
                Bokmal to txt,
                Nynorsk to txt,
            )
        )

        assertEquals(expected, element)
    }

    @Test
    fun `plain text with three languages is created`() {
        val txt = "hei"
        val element = ParagraphOnlyScope<LangBokmalNynorskEnglish, SomeDto>().apply {
            text(
                bokmal { +txt },
                nynorsk { +txt },
                english { +txt },
            )
        }.elements.first()

        val expected = Content(
            Element.OutlineContent.ParagraphContent.Text.Literal.create(
                Bokmal to txt,
                Nynorsk to txt,
                English to txt,
            )
        )

        assertEquals(expected, element)
    }

    @Test
    fun `bold text with single language is created`() {
        val txt = "hei"
        val element = ParagraphOnlyScope<LangNynorsk, SomeDto>().apply {
            text(
                nynorsk { +txt },
                FontType.BOLD
            )
        }.elements.first()

        val expected = Content(
            Element.OutlineContent.ParagraphContent.Text.Literal.create(
                Nynorsk to txt,
                FontType.BOLD
            )
        )

        assertEquals(expected, element)
    }

    @Test
    fun `bold text with two languages is created`() {
        val txt = "hei"
        val element = ParagraphOnlyScope<LanguageSupport.Double<Nynorsk, English>, SomeDto>().apply {
            text(
                nynorsk { +txt },
                english { +txt },
                FontType.BOLD
            )
        }.elements.first()

        val expected = Content(
            Element.OutlineContent.ParagraphContent.Text.Literal.create(
                Nynorsk to txt,
                English to txt,
                FontType.BOLD
            )
        )

        assertEquals(expected, element)
    }

    @Test
    fun `bold text with three languages is created`() {
        val txt = "hei"
        val element = ParagraphOnlyScope<LangBokmalNynorskEnglish, SomeDto>().apply {
            text(
                bokmal { +txt },
                nynorsk { +txt },
                english { +txt },
                FontType.BOLD
            )
        }.elements.first()

        val expected = Content(
            Element.OutlineContent.ParagraphContent.Text.Literal.create(
                Bokmal to txt,
                Nynorsk to txt,
                English to txt,
                FontType.BOLD
            )
        )

        assertEquals(expected, element)
    }

    @Test
    fun `text with three languages is created`() {
        val txt = "hei"
        val element = ParagraphOnlyScope<LangBokmalNynorskEnglish, SomeDto>().apply {
            text(
                bokmal { +txt },
                nynorsk { +txt },
                english { +txt },
            )
        }.elements.first()

        val expected = Content(
            Element.OutlineContent.ParagraphContent.Text.Literal.create(
                Bokmal to txt,
                Nynorsk to txt,
                English to txt,
                FontType.PLAIN
            )
        )

        assertEquals(expected, element)
    }

    @Test
    fun `text expression is created when at least one text is an expression`() {
        val txt = "hei"
        val element = ParagraphOnlyScope<LangBokmalNynorskEnglish, SomeDto>().apply {
            text(
                bokmal { +txt.expr() },
                nynorsk { +txt },
                english { +txt },
            )
        }.elements.first()

        val expected = Content(
            Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(
                Bokmal to txt.expr(),
                Nynorsk to txt.expr(),
                English to txt.expr(),
                FontType.PLAIN
            )
        )

        assertEquals(expected, element)
    }

    @Test
    fun `text expression is created when every text is an expression`() {
        val txt = "hei"
        val element = ParagraphOnlyScope<LangBokmalNynorskEnglish, SomeDto>().apply {
            text(
                bokmal { +txt.expr() },
                nynorsk { +txt.expr() },
                english { +txt.expr() },
            )
        }.elements.first()

        val expected = Content(
            Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(
                Bokmal to txt.expr(),
                Nynorsk to txt.expr(),
                English to txt.expr(),
                FontType.PLAIN
            )
        )

        assertEquals(expected, element)
    }

    @Test
    fun `unary plus on new line within the builder adds the content together when using expressions`() {
        val txt = "hei"
        val element = ParagraphOnlyScope<LangBokmalNynorskEnglish, SomeDto>().apply {
            text(
                bokmal {
                    +txt.expr()
                    +txt.expr()
                },
                nynorsk {
                    +txt.expr()
                    +txt.expr()
                },
                english {
                    +txt.expr()
                    +txt.expr()
                },
            )
        }.elements.first()

        val expected = Content(
            Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(
                Bokmal to txt.expr() + txt.expr(),
                Nynorsk to txt.expr() + txt.expr(),
                English to txt.expr() + txt.expr(),
                FontType.PLAIN
            )
        )

        assertEquals(expected, element)
    }

    @Test
    fun `unary plus on new line within the builder adds the content together when using literals`() {
        val element = ParagraphOnlyScope<LangBokmalNynorskEnglish, SomeDto>().apply {
            text(
                bokmal {
                    +"hei"
                    +"hei"
                },
                nynorsk {
                    +"hei"
                    +"hei"
                },
                english {
                    +"hei"
                    +"hei"
                },
            )
        }.elements.first()

        val expected = Content(
            Element.OutlineContent.ParagraphContent.Text.Literal.create(
                Bokmal to "heihei",
                Nynorsk to "heihei",
                English to "heihei",
                FontType.PLAIN
            )
        )

        assertEquals(expected, element)
    }



}