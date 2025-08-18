package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.ContentOrControlStructure.Content
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.dsl.expression.expr
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TemplateTextTest {

    @Test
    fun `universalTextExpr legger til text element`() {
        val actual = TextOnlyScope<LangBokmal, Unit>().apply { universalTextExpr("expr".expr()) }

        assertEquals(
            listOf(Content(Element.OutlineContent.ParagraphContent.Text.Expression<LangBokmalNynorskEnglish>(
                "expr".expr()))),
            actual.elements
        )
    }

    fun `universalText legger til text element`() {
        val actual = TextOnlyScope<LangBokmal, Unit>().apply { universalText("expr") }

        assertEquals(
            listOf(Content(Element.OutlineContent.ParagraphContent.Text.Expression<LangBokmalNynorskEnglish>(
                "expr".expr()))),
            actual.elements
        )
    }
}