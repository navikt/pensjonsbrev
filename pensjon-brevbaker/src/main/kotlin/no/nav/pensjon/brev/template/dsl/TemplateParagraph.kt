package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.expression.*


@LetterTemplateMarker
class ParagraphScope<Lang : LanguageSupport, LetterData : Any> : ParagraphScopeBase<Lang, LetterData, ParagraphScope<Lang, LetterData>>() {

    fun includePhrase(phrase: ParagraphPhrase<out Lang, Unit>) {
        phrase.apply(this, Unit.expr())
    }

    fun <PhraseData> includePhrase(phrase: ParagraphPhrase<out Lang, PhraseData>, data: Expression<PhraseData>) {
        phrase.apply(this, data)
    }

    override fun scopeFactory(): ParagraphScope<Lang, LetterData> = ParagraphScope()

}

abstract class ParagraphScopeBase<Lang : LanguageSupport, LetterData : Any, Scope : ParagraphScopeBase<Lang, LetterData, Scope>> :
    TextOnlyScopeBase<Lang, LetterData, Scope>() {

    @Deprecated(message = "Phrase klassen er erstattet med TextOnlyPhrase, ParagraphPhrase, OutlinePhrase klassene for å støtte fraser i forskjellige scopes", replaceWith = ReplaceWith("includePhrase(phrase, argument)"))
    fun <PhraseData : Any> includePhrase(argument: Expression<PhraseData>, phrase: Phrase<Lang, PhraseData>) {
        children.add(Element.IncludePhrase(argument, phrase))
    }

    @Deprecated(message = "Phrase klassen er erstattet med TextOnlyPhrase, ParagraphPhrase, OutlinePhrase klassene for å støtte fraser i forskjellige scopes", replaceWith = ReplaceWith("includePhrase(phrase)"))
    fun includePhrase(phrase: Phrase<Lang, Unit>) {
        children.add(Element.IncludePhrase(Unit.expr(), phrase))
    }

    fun list(init: ListRootScope<Lang, LetterData>.() -> Unit) {
        children.add(Element.ItemList(ListRootScope<Lang, LetterData>().apply(init).children))
    }

    fun table(header: TableHeaderScope<Lang, LetterData>.() -> Unit,
              init: TableRootScope<Lang, LetterData>.() -> Unit) {
        children.add(
            Element.Table(
                rows = TableRootScope<Lang, LetterData>().apply(init).rows,
                header = Element.Table.Header(TableHeaderScope<Lang, LetterData>().apply(header).children)
            )
        )
    }

    fun formText(size: Int, prompt: Element.Text<Lang>, vspace: Boolean = true) {
        children.add(Element.Form.Text(prompt, size, vspace))
    }

    fun formChoice(
        prompt: Element.Text<Lang>,
        vspace: Boolean = true,
        init: TemplateFormChoiceScope<Lang, LetterData>.() -> Unit
    ) {
        TemplateFormChoiceScope<Lang, LetterData>().apply(init)
            .let { Element.Form.MultipleChoice(prompt, it.choices, vspace) }
            .also { children.add(it) }
    }

    fun <E1: Any> ifNotNull(expr1: Expression<E1?>, block: ParagraphScope<Lang, LetterData>.(Expression<E1>) -> Unit) {
        children.add(
            Element.Conditional(expr1.notNull(), ParagraphScope<Lang, LetterData>().apply {
                // Følgende er en trygg cast fordi `children` blir kun brukt om `expr1.notNull()` evaluerer til true.
                @Suppress("UNCHECKED_CAST")
                block(this, expr1 as Expression<E1>)
            }.children, emptyList())
        )
    }

    fun <E1: Any, E2: Any> ifNotNull(expr1: Expression<E1?>, expr2: Expression<E2?>, block: ParagraphScope<Lang, LetterData>.(Expression<E1>, Expression<E2>) -> Unit) {
        children.add(
            Element.Conditional(expr1.notNull() and expr2.notNull(), ParagraphScope<Lang, LetterData>().apply {
                // Følgende er en trygg cast fordi `children` blir kun brukt om `expr1.notNull() and expr2.notNull()` evaluerer til true.
                @Suppress("UNCHECKED_CAST")
                block(this, expr1 as Expression<E1>, expr2 as Expression<E2>)
            }.children, emptyList())
        )
    }

    fun <E1: Any, E2: Any, E3: Any> ifNotNull(expr1: Expression<E1?>, expr2: Expression<E2?>, expr3: Expression<E3?>, block: ParagraphScope<Lang, LetterData>.(Expression<E1>, Expression<E2>, Expression<E3>) -> Unit) {
        children.add(
            Element.Conditional(expr1.notNull() and expr2.notNull() and expr3.notNull(), ParagraphScope<Lang, LetterData>().apply {
                // Følgende er en trygg cast fordi `children` blir kun brukt om `expr1.notNull() and expr2.notNull() and expr3.notNull()` evaluerer til true.
                @Suppress("UNCHECKED_CAST")
                block(this, expr1 as Expression<E1>, expr2 as Expression<E2>, expr3 as Expression<E3>)
            }.children, emptyList())
        )
    }
}
