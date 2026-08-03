package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*


@LetterTemplateMarker
class ListScope<Lang : LanguageSupport, LetterData : Any> internal constructor(): ControlStructureScope<Lang, LetterData, Element.OutlineContent.ParagraphContent.ItemList.Item<Lang>, ListScope<Lang, LetterData>> {
    @BrevbakerDSLInternal override val elements: List<ListItemElement<Lang>> field = mutableListOf<ListItemElement<Lang>>()
    @BrevbakerDSLInternal override fun scopeFactory(): ListScope<Lang, LetterData> = ListScope()

    @BrevbakerDSLInternal
    override fun addControlStructure(e: ListItemElement<Lang>) {
        elements.add(e)
    }

    fun item(create: TextOnlyScope<Lang, LetterData>.() -> Unit) {
        TextOnlyScope<Lang, LetterData>().apply(create)
            .let { Element.OutlineContent.ParagraphContent.ItemList.Item(it.elements) }
            .let { ContentOrControlStructure.Content(it) }
            .also { elements.add(it) }
    }

}