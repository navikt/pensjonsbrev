package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*


@LetterTemplateMarker
class OutlineOnlyScope<Lang : LanguageSupport, LetterData : Any> : OutlineScope<Lang, LetterData>, ControlStructureScope<Lang, LetterData, Element<Lang>, OutlineOnlyScope<Lang, LetterData>> {
    private val children = mutableListOf<AnyElement<Lang>>()
    override val elements: List<AnyElement<Lang>>
        get() = children

    override fun scopeFactory(): OutlineOnlyScope<Lang, LetterData> = OutlineOnlyScope()

    override fun addControlStructure(e: AnyElement<Lang>) {
        children.add(e)
    }

    override fun addTextContent(e: TextElement<Lang>) {
        children.add(e)
    }

    override fun addParagraphContent(e: ParagraphContentElement<Lang>) {
        children.add(e)
    }

    override fun addOutlineContent(e: AnyElement<Lang>) {
        children.add(e)
    }

    fun includePhrase(phrase: OutlinePhrase<out Lang>) {
        phrase.apply(this)
    }

}

interface OutlineScope<Lang : LanguageSupport, LetterData : Any> : ParagraphScope<Lang, LetterData> {
    fun addOutlineContent(e: AnyElement<Lang>)

    fun title1(create: TextOnlyScope<Lang, LetterData>.() -> Unit) {
        TextOnlyScope<Lang, LetterData>().apply(create)
            .let { Element.Title1(it.elements) }
            .let { ContentOrControlStructure.Content(it) }
            .also { addOutlineContent(it) }
    }

    fun paragraph(create: ParagraphOnlyScope<Lang, LetterData>.() -> Unit) {
        ParagraphOnlyScope<Lang, LetterData>().apply(create)
            .let { Element.Paragraph(it.elements) }
            .let { ContentOrControlStructure.Content(it) }
            .also { addOutlineContent(it) }
    }
}
