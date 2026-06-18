package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*


@LetterTemplateMarker
class OutlineOnlyScope<Lang : LanguageSupport, LetterData : Any> internal constructor(): OutlineScope<Lang, LetterData>, ControlStructureScope<Lang, LetterData, Element.OutlineContent<Lang>, OutlineOnlyScope<Lang, LetterData>> {
    private val _elements = mutableListOf<OutlineElement<Lang>>()
    @BrevbakerDSLInternal override val elements: List<OutlineElement<Lang>> get() = _elements
    @BrevbakerDSLInternal override fun scopeFactory(): OutlineOnlyScope<Lang, LetterData> = OutlineOnlyScope()
    @BrevbakerDSLInternal override fun addControlStructure(e: OutlineElement<Lang>) = addOutlineContent(e)

    @BrevbakerDSLInternal
    override fun addOutlineContent(e: OutlineElement<Lang>) {
        _elements.add(e)
    }

    fun includePhrase(phrase: OutlinePhrase<out Lang>) {
        phrase.apply(this)
    }
}

sealed interface OutlineScope<Lang : LanguageSupport, LetterData : Any> {
    @BrevbakerDSLInternal fun addOutlineContent(e: OutlineElement<Lang>)

    fun title1(create: PlainTextOnlyScope<Lang, LetterData>.() -> Unit) {
        PlainTextOnlyScope<Lang, LetterData>().apply(create)
            .let { Element.OutlineContent.Title1(it.elements) }
            .let { ContentOrControlStructure.Content(it) }
            .also { addOutlineContent(it) }
    }

    fun title2(create: PlainTextOnlyScope<Lang, LetterData>.() -> Unit) {
        PlainTextOnlyScope<Lang, LetterData>().apply(create)
            .let { Element.OutlineContent.Title2(it.elements) }
            .let { ContentOrControlStructure.Content(it) }
            .also { addOutlineContent(it) }
    }

    fun title3(create: PlainTextOnlyScope<Lang, LetterData>.() -> Unit) {
        PlainTextOnlyScope<Lang, LetterData>().apply(create)
            .let { Element.OutlineContent.Title3(it.elements) }
            .let { ContentOrControlStructure.Content(it) }
            .also { addOutlineContent(it) }
    }

    fun paragraph(create: ParagraphOnlyScope<Lang, LetterData>.() -> Unit) {
        ParagraphOnlyScope<Lang, LetterData>().apply(create)
            .let { Element.OutlineContent.Paragraph(it.elements) }
            .let { ContentOrControlStructure.Content(it) }
            .also { addOutlineContent(it) }
    }
}
