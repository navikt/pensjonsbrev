package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.expression.expr


@LetterTemplateMarker
class TextOnlyScope<Lang : LanguageSupport, LetterData : Any>(children: MutableList<Element<Lang>> = mutableListOf()) :
    TextOnlyScopeBase<Lang, LetterData, TextOnlyScope<Lang, LetterData>>(children) {

    fun includePhrase(phrase: TextOnlyPhrase<out Lang, Unit>) {
        phrase.apply(this, Unit.expr())
    }

    fun <PhraseData> includePhrase(phrase: TextOnlyPhrase<out Lang, PhraseData>, data: Expression<PhraseData>) {
        phrase.apply(this, data)
    }

    override fun scopeFactory(): TextOnlyScope<Lang, LetterData> = TextOnlyScope()

}

abstract class TextOnlyScopeBase<Lang : LanguageSupport, LetterData : Any, Scope : TextOnlyScopeBase<Lang, LetterData, Scope>>(
    children: MutableList<Element<Lang>> = mutableListOf()
) : BaseControlStructureScope<Lang, LetterData, Scope>(children) {

    fun addAll(items: List<Element<Lang>>) {
        children.addAll(items)
    }

    // TODO: Consider removing this since textExpr already supports this, or renaming to allLanguagesExpr (or something similar).
    fun eval(expression: StringExpression) {
        children.add(Element.Text.Expression(expression))
    }

    fun eval(expressionInit: () -> StringExpression) {
        children.add(Element.Text.Expression(expressionInit()))
    }

    fun newline() {
        children.add(Element.NewLine())
    }
}

// TextOnlyBuilder.text()
//
//
fun <Lang1 : Language, ParameterType : Any, Scope : TextOnlyScopeBase<LanguageSupport.Single<Lang1>, ParameterType, Scope>> Scope.text(
    lang1: Pair<Lang1, String>, fontType: Element.Text.FontType = Element.Text.FontType.PLAIN
) {
    Element.Text.Literal.create(lang1, fontType).also { children.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, ParameterType : Any, Scope : TextOnlyScopeBase<LanguageSupport.Double<Lang1, Lang2>, ParameterType, Scope>> Scope.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    fontType: Element.Text.FontType = Element.Text.FontType.PLAIN,
) {
    Element.Text.Literal.create(lang1, lang2, fontType).also { children.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, ParameterType : Any, Scope : TextOnlyScopeBase<LanguageSupport.Triple<Lang1, Lang2, Lang3>, ParameterType, Scope>> Scope.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
    fontType: Element.Text.FontType = Element.Text.FontType.PLAIN,
) {
    Element.Text.Literal.create(lang1, lang2, lang3, fontType).also { children.add(it) }
}

// TextOnlyBuilder.textExpr()
//
//
fun <Lang1 : Language, ParameterType : Any, Scope : TextOnlyScopeBase<LanguageSupport.Single<Lang1>, ParameterType, Scope>> Scope.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    fontType: Element.Text.FontType = Element.Text.FontType.PLAIN,
) {
    Element.Text.Expression.ByLanguage.create(lang1, fontType).also { children.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, ParameterType : Any, Scope : TextOnlyScopeBase<LanguageSupport.Double<Lang1, Lang2>, ParameterType, Scope>> Scope.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    lang2: Pair<Lang2, StringExpression>,
    fontType: Element.Text.FontType = Element.Text.FontType.PLAIN,
) {
    Element.Text.Expression.ByLanguage.create(lang1, lang2, fontType).also { children.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, ParameterType : Any, Scope : TextOnlyScopeBase<LanguageSupport.Triple<Lang1, Lang2, Lang3>, ParameterType, Scope>> Scope.textExpr(
    lang1: Pair<Lang1, StringExpression>,
    lang2: Pair<Lang2, StringExpression>,
    lang3: Pair<Lang3, StringExpression>,
    fontType: Element.Text.FontType = Element.Text.FontType.PLAIN,
) {
    Element.Text.Expression.ByLanguage.create(lang1, lang2, lang3, fontType).also { children.add(it) }
}

