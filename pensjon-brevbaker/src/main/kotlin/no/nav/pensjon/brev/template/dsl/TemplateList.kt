package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LanguageSupport


@LetterTemplateMarker
open class ListRootScope<Lang : LanguageSupport, LetterData : Any>
    : ListBaseScope<Lang, LetterData>() {
    fun showIf(
        predicate: Expression<Boolean>,
        showIf: ListBaseScope<Lang, LetterData>.() -> Unit
    ) {
        ListBaseScope<Lang, LetterData>().apply(showIf).also { builder ->
            children.addAll(builder.children.map {
                it.copy(condition = predicate)
            })
        }
    }
}

@LetterTemplateMarker
open class ListBaseScope<Lang : LanguageSupport, LetterData : Any>(
    val children: MutableList<Element.ItemList.Item<Lang>> = mutableListOf()
) : TemplateGlobalScope<LetterData>() {
    fun item(
        init: TextOnlyScope<Lang, LetterData>.() -> Unit
    ) {
        children.add(Element.ItemList.Item(TextOnlyScope<Lang, LetterData>().apply(init).children))
    }
}

