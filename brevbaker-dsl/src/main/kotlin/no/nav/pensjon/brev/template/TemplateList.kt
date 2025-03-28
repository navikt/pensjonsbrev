package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*


@LetterTemplateMarker
class ListScope<Lang : LanguageSupport, LetterData : Any> internal constructor(): ControlStructureScope<Lang, LetterData, Element.OutlineContent.ParagraphContent.ItemList.Item<Lang>, ListScope<Lang, LetterData>> {
    private val children = mutableListOf<ListItemElement<Lang>>()
    override val elements: List<ListItemElement<Lang>>
        get() = children

    override fun scopeFactory(): ListScope<Lang, LetterData> = ListScope()

    override fun addControlStructure(e: ListItemElement<Lang>) {
        children.add(e)
    }

    fun item(create: TextOnlyScope<Lang, LetterData>.() -> Unit) {
        TextOnlyScope<Lang, LetterData>().apply(create)
            .let { Element.OutlineContent.ParagraphContent.ItemList.Item(it.elements) }
            .let { ContentOrControlStructure.Content(it) }
            .also { children.add(it) }
    }

}