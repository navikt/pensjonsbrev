package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.ContentOrControlStructure.*
import no.nav.pensjon.brev.template.ContentOrControlStructureImpl.ContentImpl
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType

@LetterTemplateMarker
class TextOnlyScope<Lang : LanguageSupport, LetterData : Any> : TextScope<Lang, LetterData>, ControlStructureScope<Lang, LetterData, Element.OutlineContent.ParagraphContent.Text<Lang>, TextOnlyScope<Lang, LetterData>> {
    private val children = mutableListOf<TextElement<Lang>>()
    override val elements: List<TextElement<Lang>>
        get() = children

    override fun scopeFactory(): TextOnlyScope<Lang, LetterData> = TextOnlyScope()

    override fun addControlStructure(e: TextElement<Lang>) {
        children.add(e)
    }

    override fun addTextContent(e: TextElement<Lang>) {
        children.add(e)
    }

    override fun newline() {
        addTextContent(ContentImpl(ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.NewLineImpl(children.size)))
    }

    fun includePhrase(phrase: TextOnlyPhrase<out Lang>) {
        phrase.apply(this)
    }
}

@LetterTemplateMarker
class PlainTextOnlyScope<Lang : LanguageSupport, LetterData : Any> : PlainTextScope<Lang, LetterData>, ControlStructureScope<Lang, LetterData, Element.OutlineContent.ParagraphContent.Text<Lang>, PlainTextOnlyScope<Lang, LetterData>> {
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

}

interface PlainTextScope<Lang : LanguageSupport, LetterData : Any> : TemplateGlobalScope<LetterData> {
    fun addTextContent(e: TextElement<Lang>)
    fun eval(expression: StringExpression) {
        addTextContent(ContentImpl(ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.ExpressionImpl(expression, FontType.PLAIN)))
    }
}

// TextScope.text()
//
//
fun <Lang1 : Language, ParameterType : Any> TextScope<LanguageSupport.Single<Lang1>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
    fontType: FontType = FontType.PLAIN,
) {
    ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.LiteralImpl.create(lang1, fontType).also { addTextContent(ContentImpl(it)) }
}

fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> TextScope<LanguageSupport.Double<Lang1, Lang2>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    fontType: FontType = FontType.PLAIN,
) {
    ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.LiteralImpl.create(lang1, lang2, fontType).also { addTextContent(ContentImpl(it)) }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, ParameterType : Any> TextScope<LanguageSupport.Triple<Lang1, Lang2, Lang3>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
    fontType: FontType = FontType.PLAIN,
) {
    ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.LiteralImpl.create(lang1, lang2, lang3, fontType).also { addTextContent(ContentImpl(it)) }
}

// TextScope.textExpr()
//
//
fun <Lang1 : Language, ParameterType : Any> TextScope<LanguageSupport.Single<Lang1>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    fontType: FontType = FontType.PLAIN,
) {
    ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.ExpressionImpl.ByLanguageImpl.create(lang1, fontType).also { addTextContent(ContentImpl(it)) }
}

fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> TextScope<LanguageSupport.Double<Lang1, Lang2>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    lang2: Pair<Lang2, StringExpression>,
    fontType: FontType = FontType.PLAIN,
) {
    ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.ExpressionImpl.ByLanguageImpl.create(lang1, lang2, fontType).also { addTextContent(ContentImpl(it)) }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, ParameterType : Any> TextScope<LanguageSupport.Triple<Lang1, Lang2, Lang3>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    lang2: Pair<Lang2, StringExpression>,
    lang3: Pair<Lang3, StringExpression>,
    fontType: FontType = FontType.PLAIN,
) {
    ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.ExpressionImpl.ByLanguageImpl.create(lang1, lang2, lang3, fontType).also { addTextContent(ContentImpl(it)) }
}


// PlainTextScope.text()
//
//
fun <Lang1 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Single<Lang1>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
) {
    ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.LiteralImpl.create(lang1).also { addTextContent(ContentImpl(it)) }
}

fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Double<Lang1, Lang2>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
) {
    ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.LiteralImpl.create(lang1, lang2).also { addTextContent(ContentImpl(it)) }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Triple<Lang1, Lang2, Lang3>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
) {
    ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.LiteralImpl.create(lang1, lang2, lang3).also { addTextContent(ContentImpl(it)) }
}

// PlainTextScope.textExpr()
//
//
fun <Lang1 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Single<Lang1>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
) {
    ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.ExpressionImpl.ByLanguageImpl.create(lang1).also { addTextContent(ContentImpl(it)) }
}

fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Double<Lang1, Lang2>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    lang2: Pair<Lang2, StringExpression>,
) {
    ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.ExpressionImpl.ByLanguageImpl.create(lang1, lang2).also { addTextContent(ContentImpl(it)) }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Triple<Lang1, Lang2, Lang3>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    lang2: Pair<Lang2, StringExpression>,
    lang3: Pair<Lang3, StringExpression>,
) {
    ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.ExpressionImpl.ByLanguageImpl.create(lang1, lang2, lang3).also { addTextContent(ContentImpl(it)) }
}
