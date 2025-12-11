package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.ContentOrControlStructure.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.dsl.LiteralOrExpressionBuilder.*
import no.nav.pensjon.brev.template.dsl.TextContentCreator.createTextContent

@LetterTemplateMarker
class TextOnlyScope<Lang : LanguageSupport, LetterData : Any> internal constructor(): TextScope<Lang, LetterData>, ControlStructureScope<Lang, LetterData, Element.OutlineContent.ParagraphContent.Text<Lang>, TextOnlyScope<Lang, LetterData>> {
    private val children = mutableListOf<TextElement<Lang>>()
    override val elements: List<TextElement<Lang>>
        get() = children

    override fun scopeFactory(): TextOnlyScope<Lang, LetterData> = TextOnlyScope()

    override fun addControlStructure(e: TextElement<Lang>) {
        children.add(e)
    }

    override fun addTextContentBaseLanguages(e: TextElement<BaseLanguages>) {
        // Safe because we know that a template that support BaseLanguages will support Lang
        @Suppress("UNCHECKED_CAST")
        children.add(e as TextElement<Lang>)
    }

    override fun addTextContent(e: TextElement<Lang>) {
        children.add(e)
    }

    override fun newline() {
        addTextContent(Content(Element.OutlineContent.ParagraphContent.Text.NewLine<Lang>(children.size)))
    }

    fun includePhrase(phrase: TextOnlyPhrase<out Lang>) {
        phrase.apply(this)
    }

    fun includePhrase(phrase: PlainTextOnlyPhrase<out Lang>) {
        phrase.apply(this)
    }
}

@LetterTemplateMarker
class PlainTextOnlyScope<Lang : LanguageSupport, LetterData : Any> internal constructor(): PlainTextScope<Lang, LetterData>, ControlStructureScope<Lang, LetterData, Element.OutlineContent.ParagraphContent.Text<Lang>, PlainTextOnlyScope<Lang, LetterData>> {
    private val children = mutableListOf<TextElement<Lang>>()
    override val elements: List<TextElement<Lang>>
        get() = children

    override fun scopeFactory(): PlainTextOnlyScope<Lang, LetterData> = PlainTextOnlyScope()

    override fun addControlStructure(e: TextElement<Lang>) {
        children.add(e)
    }

    override fun addTextContent(e: TextElement<Lang>) {
        children.add(e)
    }

    fun includePhrase(phrase: PlainTextOnlyPhrase<out Lang>) {
        phrase.apply(this)
    }
}

sealed interface PlainTextScope<Lang : LanguageSupport, LetterData : Any> : TemplateGlobalScope<LetterData> {
    fun addTextContent(e: TextElement<Lang>)
    fun eval(expression: StringExpression) {
        addTextContent(Content(Element.OutlineContent.ParagraphContent.Text.Expression(expression, FontType.PLAIN)))
    }

}

fun <Lang1 : Language, ParameterType : Any> TextScope<LanguageSupport.Single<Lang1>, ParameterType>.text(
    lang1: Pair<Lang1, LiteralOrExpression>,
    fontType: FontType = FontType.PLAIN,
    ) {
    addTextContent(Content(createTextContent(lang1, fontType)))
}


fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> TextScope<LanguageSupport.Double<Lang1, Lang2>, ParameterType>.text(
    lang1: Pair<Lang1, LiteralOrExpression>,
    lang2: Pair<Lang2, LiteralOrExpression>,
    fontType: FontType = FontType.PLAIN,
    ) {
    addTextContent(Content(createTextContent(lang1, lang2, fontType)))
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, ParameterType : Any> TextScope<LanguageSupport.Triple<Lang1, Lang2, Lang3>, ParameterType>.text(
    lang1: Pair<Lang1, LiteralOrExpression>,
    lang2: Pair<Lang2, LiteralOrExpression>,
    lang3: Pair<Lang3, LiteralOrExpression>,
    fontType: FontType = FontType.PLAIN,
    ) {
    addTextContent(Content(createTextContent(lang1, lang2, lang3, fontType)))
}

fun <Lang1 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Single<Lang1>, ParameterType>.text(
    lang1: Pair<Lang1, LiteralOrExpression>,
) {
    addTextContent(Content(createTextContent(lang1, FontType.PLAIN)))
}

fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Double<Lang1, Lang2>, ParameterType>.text(
    lang1: Pair<Lang1, LiteralOrExpression>,
    lang2: Pair<Lang2, LiteralOrExpression>,
) {
    addTextContent(Content(createTextContent(lang1, lang2, FontType.PLAIN)))
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Triple<Lang1, Lang2, Lang3>, ParameterType>.text(
    lang1: Pair<Lang1, LiteralOrExpression>,
    lang2: Pair<Lang2, LiteralOrExpression>,
    lang3: Pair<Lang3, LiteralOrExpression>,
) {
    addTextContent(Content(createTextContent(lang1, lang2, lang3, FontType.PLAIN)))}


internal object TextContentCreator {
    internal fun <Lang1 : Language> createTextContent(
        lang1: Pair<Lang1, LiteralOrExpression>,
        fontType: FontType
    ): Element.OutlineContent.ParagraphContent.Text<LanguageSupport.Single<Lang1>> = when (val value = lang1.second) {
        is ExpressionWrapper ->
            Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(lang1.first to value.expr, fontType)

        is LiteralWrapper ->
            Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1.first to value.str, fontType)
    }

    internal fun <Lang1 : Language, Lang2 : Language> createTextContent(
        lang1: Pair<Lang1, LiteralOrExpression>,
        lang2: Pair<Lang2, LiteralOrExpression>,
        fontType: FontType
    ): Element.OutlineContent.ParagraphContent.Text<LanguageSupport.Double<Lang1, Lang2>> {
        val lang1Value = lang1.second
        val lang2Value = lang2.second

        val textContent = if (lang1Value is LiteralWrapper && lang2Value is LiteralWrapper) {
            Element.OutlineContent.ParagraphContent.Text.Literal.create(
                lang1.first to lang1Value.str,
                lang2.first to lang2Value.str,
                fontType
            )
        } else {
            Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(
                lang1.first to lang1.second.expr,
                lang2.first to lang2.second.expr,
                fontType
            )
        }
        return textContent
    }

    internal fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> createTextContent(
        lang1: Pair<Lang1, LiteralOrExpression>,
        lang2: Pair<Lang2, LiteralOrExpression>,
        lang3: Pair<Lang3, LiteralOrExpression>,
        fontType: FontType
    ): Element.OutlineContent.ParagraphContent.Text<LanguageSupport.Triple<Lang1, Lang2, Lang3>> {
        val lang1Value = lang1.second
        val lang2Value = lang2.second
        val lang3Value = lang3.second

        val textContent =
            if (lang1Value is LiteralWrapper && lang2Value is LiteralWrapper && lang3Value is LiteralWrapper) {
                Element.OutlineContent.ParagraphContent.Text.Literal.create(
                    lang1.first to lang1Value.str,
                    lang2.first to lang2Value.str,
                    lang3.first to lang3Value.str,
                    fontType
                )
            } else {
                Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(
                    lang1.first to lang1Value.expr,
                    lang2.first to lang2Value.expr,
                    lang3.first to lang3Value.expr,
                    fontType
                )
            }
        return textContent
    }
}