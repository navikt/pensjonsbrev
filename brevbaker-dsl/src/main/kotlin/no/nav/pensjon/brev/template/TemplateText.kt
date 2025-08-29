package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.ContentOrControlStructure.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.dsl.LiteralOrExpressionBuilder.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus

@LetterTemplateMarker
class TextOnlyScope<Lang : LanguageSupport, LetterData : Any> internal constructor(): TextScope<Lang, LetterData>, ControlStructureScope<Lang, LetterData, Element.OutlineContent.ParagraphContent.Text<Lang>, TextOnlyScope<Lang, LetterData>> {
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
        addTextContent(Content(Element.OutlineContent.ParagraphContent.Text.NewLine(children.size)))
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

    fun bokmal(block: LiteralOrExpressionBuilder.() -> LiteralOrExpression): Pair<Language.Bokmal, LiteralOrExpression> =
        Language.Bokmal to LiteralOrExpressionBuilder().block()

    fun nynorsk(block: LiteralOrExpressionBuilder.() -> LiteralOrExpression): Pair<Language.Nynorsk, LiteralOrExpression> =
        Language.Nynorsk to LiteralOrExpressionBuilder().block()

    fun english(block: LiteralOrExpressionBuilder.() -> LiteralOrExpression): Pair<Language.English, LiteralOrExpression> =
        Language.English to LiteralOrExpressionBuilder().block()
}

// TextScope.text()
//
//
@JvmName("oldText")
fun <Lang1 : Language, ParameterType : Any> TextScope<LanguageSupport.Single<Lang1>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
    fontType: FontType = FontType.PLAIN,
) {
    Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1, fontType).also { addTextContent(Content(it)) }
}

@JvmName("oldText")
fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> TextScope<LanguageSupport.Double<Lang1, Lang2>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    fontType: FontType = FontType.PLAIN,
) {
    Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1, lang2, fontType).also { addTextContent(Content(it)) }
}

@JvmName("oldText")
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

fun <Lang1 : Language, ParameterType : Any> TextScope<LanguageSupport.Single<Lang1>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    fontType: FontType = FontType.PLAIN,
) {
    Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(lang1, fontType).also { addTextContent(Content(it)) }
}

fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> TextScope<LanguageSupport.Double<Lang1, Lang2>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    lang2: Pair<Lang2, StringExpression>,
    fontType: FontType = FontType.PLAIN,
) {
    Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(lang1, lang2, fontType).also { addTextContent(Content(it)) }
}

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
fun <Lang1 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Single<Lang1>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
) {
    Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1).also { addTextContent(Content(it)) }
}

@JvmName("oldText")
fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Double<Lang1, Lang2>, ParameterType>.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
) {
    Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1, lang2).also { addTextContent(Content(it)) }
}

@JvmName("oldText")
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
fun <Lang1 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Single<Lang1>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
) {
    Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(lang1).also { addTextContent(Content(it)) }
}

fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Double<Lang1, Lang2>, ParameterType>.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    lang2: Pair<Lang2, StringExpression>,
) {
    Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(lang1, lang2).also { addTextContent(Content(it)) }
}

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
    when (val value = lang1.second) {
        is ExpressionWrapper ->
            Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(lang1.first to value.expr, fontType)
                .also { addTextContent(Content(it)) }

        is LiteralWrapper ->
            Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1.first to value.str)
                .also { addTextContent(Content(it)) }
    }
}

fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> TextScope<LanguageSupport.Double<Lang1, Lang2>, ParameterType>.text(
    lang1: Pair<Lang1, LiteralOrExpression>,
    lang2: Pair<Lang2, LiteralOrExpression>,
    fontType: FontType = FontType.PLAIN,
    ) {
    val lang1Value = lang1.second
    val lang2Value = lang2.second

    if (lang1Value is LiteralWrapper && lang2Value is LiteralWrapper) {
        Element.OutlineContent.ParagraphContent.Text.Literal.create(
            lang1.first to lang1Value.str,
            lang2.first to lang2Value.str,
            fontType
        ).also { addTextContent(Content(it)) }
    } else {
        Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(
            lang1.first to lang1.second.expr,
            lang2.first to lang2.second.expr,
            fontType
        ).also { addTextContent(Content(it)) }
    }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, ParameterType : Any> TextScope<LanguageSupport.Triple<Lang1, Lang2, Lang3>, ParameterType>.text(
    lang1: Pair<Lang1, LiteralOrExpression>,
    lang2: Pair<Lang2, LiteralOrExpression>,
    lang3: Pair<Lang3, LiteralOrExpression>,
    fontType: FontType = FontType.PLAIN,
    ) {
    val lang1Value = lang1.second
    val lang2Value = lang2.second
    val lang3Value = lang3.second

    if (lang1Value is LiteralWrapper && lang2Value is LiteralWrapper && lang3Value is LiteralWrapper) {
        Element.OutlineContent.ParagraphContent.Text.Literal.create(
            lang1.first to lang1Value.str,
            lang2.first to lang2Value.str,
            lang3.first to lang3Value.str,
            fontType
        ).also { addTextContent(Content(it)) }
            .also { addTextContent(Content(it)) }
    } else {
        Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(
            lang1.first to lang1Value.expr,
            lang2.first to lang2Value.expr,
            lang3.first to lang3Value.expr,
            fontType
        ).also { addTextContent(Content(it)) }
    }
}

// PlainTextScope.text()
//
//

fun <Lang1 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Single<Lang1>, ParameterType>.text(
    lang1: Pair<Lang1, LiteralOrExpression>,
) {
    when (val value = lang1.second) {
        is ExpressionWrapper ->
            Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(lang1.first to value.expr)
                .also { addTextContent(Content(it)) }

        is LiteralWrapper ->
            Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1.first to value.str)
                .also { addTextContent(Content(it)) }
    }
}

fun <Lang1 : Language, Lang2 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Double<Lang1, Lang2>, ParameterType>.text(
    lang1: Pair<Lang1, LiteralOrExpression>,
    lang2: Pair<Lang2, LiteralOrExpression>,
) {
    val lang1Value = lang1.second
    val lang2Value = lang2.second

    if (lang1Value is LiteralWrapper && lang2Value is LiteralWrapper) {
        Element.OutlineContent.ParagraphContent.Text.Literal.create(
            lang1.first to lang1Value.str,
            lang2.first to lang2Value.str
        ).also { addTextContent(Content(it)) }
    } else {
        Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(
            lang1.first to lang1.second.expr,
            lang2.first to lang2.second.expr,
        ).also { addTextContent(Content(it)) }
    }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, ParameterType : Any> PlainTextScope<LanguageSupport.Triple<Lang1, Lang2, Lang3>, ParameterType>.text(
    lang1: Pair<Lang1, LiteralOrExpression>,
    lang2: Pair<Lang2, LiteralOrExpression>,
    lang3: Pair<Lang3, LiteralOrExpression>,
) {
    val lang1Value = lang1.second
    val lang2Value = lang2.second
    val lang3Value = lang3.second

    if (lang1Value is LiteralWrapper && lang2Value is LiteralWrapper && lang3Value is LiteralWrapper) {
        Element.OutlineContent.ParagraphContent.Text.Literal.create(
            lang1.first to lang1Value.str,
            lang2.first to lang2Value.str,
            lang3.first to lang3Value.str,
        ).also { addTextContent(Content(it)) }
            .also { addTextContent(Content(it)) }
    } else {
        Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(
            lang1.first to lang1Value.expr,
            lang2.first to lang2Value.expr,
            lang3.first to lang3Value.expr,
        ).also { addTextContent(Content(it)) }
    }
}

class LiteralOrExpressionBuilder {
    sealed class LiteralOrExpression() {
        abstract val expr: StringExpression
    }
    class LiteralWrapper(val str: String) : LiteralOrExpression() {
        override val expr: StringExpression
            get() = str.expr()
    }

    class ExpressionWrapper(override val expr: StringExpression) : LiteralOrExpression()

    operator fun StringExpression.unaryPlus() = ExpressionWrapper(this)
    operator fun String.unaryPlus() = LiteralWrapper(this)
    operator fun LiteralOrExpression.plus(other: StringExpression) = when(this) {
        is ExpressionWrapper -> ExpressionWrapper(expr + other)
        is LiteralWrapper -> ExpressionWrapper(str.expr() + other)
    }
    operator fun LiteralOrExpression.plus(other: String) = when(this) {
        is ExpressionWrapper -> ExpressionWrapper(expr + other)
        is LiteralWrapper -> LiteralWrapper(str + other)
    }
}