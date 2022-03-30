package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.LanguageSupport


@LetterTemplateMarker
class ListScope<Lang : LanguageSupport, LetterData : Any>
    : ControlStructureScopeBase<Lang, LetterData, ListScope<Lang, LetterData>>() {
    fun item(
        init: TextOnlyScope<Lang, LetterData>.() -> Unit
    ) {
        children.add(Element.ItemList.Item(TextOnlyScope<Lang, LetterData>().apply(init).children))
    }

    override fun scopeFactory(): ListScope<Lang, LetterData> = ListScope()
}