package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.ContentOrControlStructure.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.dsl.LiteralOrExpressionBuilder.*

@LetterTemplateMarker
class TextOnlyScope<Lang : LanguageSupport, LetterData : Any> internal constructor(): TextScope<Lang, LetterData>, ControlStructureScope<Lang, LetterData, Element.OutlineContent.ParagraphContent.Text<Lang>, TextOnlyScope<Lang, LetterData>> {
    private val children = mutableListOf<TextElement<Lang>>()
    override val elements: List<TextElement<Lang>>
        get() = children

    override fun scopeFactory(): TextOnlyScope<Lang, LetterData> = TextOnlyScope()

    override fun addControlStructure(e: TextElement<Lang>) {
        children.add(e)
    }

    override fun addTextContent(e: TextElement<BaseLanguages>) {
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

// TextScope.text()
//
//
@JvmName("oldText")
@Deprecated("Nytt syntax for tekster! Det er nå ikke lengre forskjell på text og textExpr. Istedenfor Bokmal to ..., bruk bokmal { + ... }")
fun <Lang1 : Language, ParameterType : Any> TextScope<LanguageSupport.Single<Lang1>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
    fontType: FontType = FontType.PLAIN,
) {
    Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1, fontType).also { addTextContent(Content(it)) }
}

@JvmName("oldText")
@Deprecated("Nytt syntax for tekster! Det er nå ikke lengre forskjell på text og textExpr. Istedenfor Bokmal to ..., bruk bokmal { + ... }")
fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> TextScope<LanguageSupport.Double<Lang1, Lang2>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    fontType: FontType = FontType.PLAIN,
) {
    Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1, lang2, fontType).also { addTextContent(Content(it)) }
}

@JvmName("oldText")
@Deprecated("Nytt syntax for tekster! Det er nå ikke lengre forskjell på text og textExpr. Istedenfor Bokmal to ..., bruk bokmal { + ... }")
fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, ParameterType : Any> TextScope<LanguageSupport.Triple<Lang1, Lang2, Lang3>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
    fontType: FontType = FontType.PLAIN,
) {
    Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1, lang2, lang3, fontType).also { addTextContent(Content(it)) }
}

// TextScope.textExpr()
//
//

@Deprecated("Nytt syntax for tekster! bruk text istedenfor textExpr, og istedenfor Bokmal to ..., bruk bokmal { + ... }.")
fun <Lang1 : Language, ParameterType : Any> TextScope<LanguageSupport.Single<Lang1>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    fontType: FontType = FontType.PLAIN,
) {
    Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(lang1, fontType).also { addTextContent(Content(it)) }
}

@Deprecated("Nytt syntax for tekster! bruk text istedenfor textExpr, og istedenfor Bokmal to ..., bruk bokmal { + ... }.")
fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> TextScope<LanguageSupport.Double<Lang1, Lang2>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    lang2: Pair<Lang2, StringExpression>,
    fontType: FontType = FontType.PLAIN,
) {
    Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(lang1, lang2, fontType).also { addTextContent(Content(it)) }
}

@Deprecated("Nytt syntax for tekster! bruk text istedenfor textExpr, og istedenfor Bokmal to ..., bruk bokmal { + ... }.")
fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, ParameterType : Any> TextScope<LanguageSupport.Triple<Lang1, Lang2, Lang3>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    lang2: Pair<Lang2, StringExpression>,
    lang3: Pair<Lang3, StringExpression>,
    fontType: FontType = FontType.PLAIN,
) {
    Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(lang1, lang2, lang3, fontType).also { addTextContent(Content(it)) }
}


// PlainTextScope.text()
//
//
@JvmName("oldText")
@Deprecated("Nytt syntax for tekster! Istedenfor Bokmal to ..., bruk bokmal { + ... }")
fun <Lang1 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Single<Lang1>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
) {
    Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1).also { addTextContent(Content(it)) }
}

@JvmName("oldText")
@Deprecated("Nytt syntax for tekster! Istedenfor Bokmal to ..., bruk bokmal { + ... }")
fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Double<Lang1, Lang2>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
) {
    Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1, lang2).also { addTextContent(Content(it)) }
}

@JvmName("oldText")
@Deprecated("Nytt syntax for tekster! Istedenfor Bokmal to ..., bruk bokmal { + ... }")
fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Triple<Lang1, Lang2, Lang3>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
) {
    Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1, lang2, lang3).also { addTextContent(Content(it)) }
}

// PlainTextScope.textExpr()
//
//
@Deprecated("Nytt syntax for tekster! Det er nå ikke lengre forskjell på text og textExpr. Istedenfor Bokmal to ..., bruk bokmal { + ... }")
fun <Lang1 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Single<Lang1>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
) {
    Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(lang1).also { addTextContent(Content(it)) }
}

@Deprecated("Nytt syntax for tekster! Det er nå ikke lengre forskjell på text og textExpr. Istedenfor Bokmal to ..., bruk bokmal { + ... }")
fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Double<Lang1, Lang2>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    lang2: Pair<Lang2, StringExpression>,
) {
    Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(lang1, lang2).also { addTextContent(Content(it)) }
}

@Deprecated("Nytt syntax for tekster! Det er nå ikke lengre forskjell på text og textExpr. Istedenfor Bokmal to ..., bruk bokmal { + ... }")
fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Triple<Lang1, Lang2, Lang3>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    lang2: Pair<Lang2, StringExpression>,
    lang3: Pair<Lang3, StringExpression>,
) {
    Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(lang1, lang2, lang3).also { addTextContent(Content(it)) }
}

// new text functions that uses LiteralOrExpressionBuilder
// TextScope.text()
//
//

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

// PlainTextScope.text()
//
//

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



private fun <Lang1 : Language> createTextContent(
    lang1: Pair<Lang1, LiteralOrExpression>,
    fontType: FontType
): Element.OutlineContent.ParagraphContent.Text<LanguageSupport.Single<Lang1>> = when (val value = lang1.second) {
    is ExpressionWrapper ->
        Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(lang1.first to value.expr, fontType)

    is LiteralWrapper ->
        Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1.first to value.str, fontType)
}

private fun <Lang1 : Language, Lang2 : Language> createTextContent(
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

private fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> createTextContent(
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
