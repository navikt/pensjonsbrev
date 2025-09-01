package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.ContentOrControlStructure.Content
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.StringExpression
import no.nav.pensjon.brev.template.TextElement
import no.nav.pensjon.brev.template.dsl.LiteralOrExpressionBuilder.LiteralOrExpression

interface TextScope<Lang : LanguageSupport, LetterData : Any> : TemplateGlobalScope<LetterData> {

    fun addTextContent(e: TextElement<Lang>)

    fun eval(expression: StringExpression, fontType: FontType = FontType.PLAIN) {
        addTextContent(Content(Element.OutlineContent.ParagraphContent.Text.Expression(expression, fontType)))
    }

    fun newline()

    fun bokmal(block: LiteralOrExpressionBuilder.() -> LiteralOrExpression): Pair<Language.Bokmal, LiteralOrExpression> =
        Language.Bokmal to LiteralOrExpressionBuilder().block()

    fun nynorsk(block: LiteralOrExpressionBuilder.() -> LiteralOrExpression): Pair<Language.Nynorsk, LiteralOrExpression> =
        Language.Nynorsk to LiteralOrExpressionBuilder().block()

    fun english(block: LiteralOrExpressionBuilder.() -> LiteralOrExpression): Pair<Language.English, LiteralOrExpression> =
        Language.English to LiteralOrExpressionBuilder().block()
}