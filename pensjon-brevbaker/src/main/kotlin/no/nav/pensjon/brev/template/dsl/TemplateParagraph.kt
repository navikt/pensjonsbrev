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
        children.add(Element.ItemList(ListRootScope<Lang, LetterData>().apply(init).items))
    }

    fun table(header: TableHeaderScope<Lang, LetterData>.() -> Unit,
              init: TableBaseScope<Lang, LetterData>.() -> Unit) {
        children.add(
            Element.Table(
                rows = TableBaseScope<Lang, LetterData>().apply(init).rows,
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
}
