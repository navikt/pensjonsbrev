package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*


@LetterTemplateMarker
class OutlineScope<Lang : LanguageSupport, LetterData : Any> : OutlineScopeBase<Lang, LetterData, OutlineScope<Lang, LetterData>>() {

    fun includePhrase(phrase: OutlinePhrase<out Lang>) {
        phrase.apply(this)
    }

    override fun scopeFactory(): OutlineScope<Lang, LetterData> = OutlineScope()

}

abstract class OutlineScopeBase<Lang : LanguageSupport, LetterData : Any, Scope : OutlineScopeBase<Lang, LetterData, Scope>> : ParagraphScopeBase<Lang, LetterData, Scope>() {

    fun title1(init: TextOnlyScope<Lang, LetterData>.() -> Unit) {
        children.add(Element.Title1(TextOnlyScope<Lang, LetterData>().apply(init).children))
    }

    fun paragraph(init: ParagraphScope<Lang, LetterData>.() -> Unit) {
        children.add(Element.Paragraph(ParagraphScope<Lang, LetterData>().apply(init).children))
    }
}
