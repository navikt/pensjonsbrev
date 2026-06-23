package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.validation.BrevTemplateValidator

@LetterTemplateMarker
class OutlineOnlyScope<Lang : LanguageSupport, LetterData : Any> internal constructor(internal val validator: BrevTemplateValidator): OutlineScope<Lang, LetterData>, ControlStructureScope<Lang, LetterData, Element.OutlineContent<Lang>, OutlineOnlyScope<Lang, LetterData>> {
    private val _elements = mutableListOf<OutlineElement<Lang>>()
    @BrevbakerDSLInternal override val elements: List<OutlineElement<Lang>> get() = _elements
    @BrevbakerDSLInternal override fun scopeFactory(): OutlineOnlyScope<Lang, LetterData> = OutlineOnlyScope(validator.subScope())
    @BrevbakerDSLInternal override fun addControlStructure(e: OutlineElement<Lang>) = addOutlineContent(e)

    @BrevbakerDSLInternal
    override fun addOutlineContent(e: OutlineElement<Lang>) {
        validator.validate(e)
        _elements.add(e)
    }

    fun includePhrase(phrase: OutlinePhrase<out Lang>) {
        phrase.apply(this)
    }
}

sealed interface OutlineScope<Lang : LanguageSupport, LetterData : Any> {
    @BrevbakerDSLInternal fun addOutlineContent(e: OutlineElement<Lang>)

    fun title1(uniqueness: String? = null, create: PlainTextOnlyScope<Lang, LetterData>.() -> Unit) {
        PlainTextOnlyScope<Lang, LetterData>().apply(create)
            .let { Element.OutlineContent.Title1(it.elements, stableHashModifier = uniqueness) }
            .let { ContentOrControlStructure.Content(it) }
            .also { addOutlineContent(it) }
    }

    fun title2(uniqueness: String? = null, create: PlainTextOnlyScope<Lang, LetterData>.() -> Unit) {
        PlainTextOnlyScope<Lang, LetterData>().apply(create)
            .let { Element.OutlineContent.Title2(it.elements, stableHashModifier = uniqueness) }
            .let { ContentOrControlStructure.Content(it) }
            .also { addOutlineContent(it) }
    }

    fun title3(uniqueness: String? = null, create: PlainTextOnlyScope<Lang, LetterData>.() -> Unit) {
        PlainTextOnlyScope<Lang, LetterData>().apply(create)
            .let { Element.OutlineContent.Title3(it.elements, stableHashModifier = uniqueness) }
            .let { ContentOrControlStructure.Content(it) }
            .also { addOutlineContent(it) }
    }

    fun paragraph(uniqueness: String? = null, create: ParagraphOnlyScope<Lang, LetterData>.() -> Unit) {
        ParagraphOnlyScope<Lang, LetterData>().apply(create)
            .let { Element.OutlineContent.Paragraph(it.elements, stableHashModifier = uniqueness) }
            .let { ContentOrControlStructure.Content(it) }
            .also { addOutlineContent(it) }
    }
}
