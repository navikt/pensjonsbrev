package no.nav.pensjon.brev.template.dsl

import no.nav.brev.Listetype
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.ContentOrControlStructure.Content


@LetterTemplateMarker
class ParagraphOnlyScope<Lang : LanguageSupport, LetterData : Any> internal constructor(): ParagraphScope<Lang, LetterData>, ControlStructureScope<Lang, LetterData, Element.OutlineContent.ParagraphContent<Lang>, ParagraphOnlyScope<Lang, LetterData>> {
    private val _elements = mutableListOf<ParagraphContentElement<Lang>>()
    @BrevbakerDSLInternal override val elements: List<ParagraphContentElement<Lang>> get() = _elements
    @BrevbakerDSLInternal override fun scopeFactory(): ParagraphOnlyScope<Lang, LetterData> = ParagraphOnlyScope()

    @BrevbakerDSLInternal
    override fun addControlStructure(e: ParagraphContentElement<Lang>) {
        _elements.add(e)
    }

    @BrevbakerDSLInternal
    override fun addParagraphContent(e: ParagraphContentElement<Lang>) {
        _elements.add(e)
    }

    @BrevbakerDSLInternal
    override fun addTextContentBaseLanguages(e: TextElement<BaseLanguages>) {
        // Safe because we know that a template that support BaseLanguages will support Lang
        @Suppress("UNCHECKED_CAST")
        _elements.add(e as TextElement<Lang>)
    }

    @BrevbakerDSLInternal
    override fun addTextContent(e: TextElement<Lang>) {
        _elements.add(e)
    }

    override fun newline() {
        addTextContent(Content(Element.OutlineContent.ParagraphContent.Text.NewLine<Lang>(_elements.size)))
    }

    fun includePhrase(phrase: ParagraphPhrase<out Lang>) {
        phrase.apply(this)
    }

    fun includePhrase(phrase: TextOnlyPhrase<out Lang>) {
        phrase.apply(this)
    }

    fun includePhrase(phrase: PlainTextOnlyPhrase<out Lang>) {
        phrase.apply(this)
    }
}

sealed interface ParagraphScope<Lang : LanguageSupport, LetterData : Any> : TextScope<Lang, LetterData> {
    fun addParagraphContent(e: ParagraphContentElement<Lang>)

    fun list(create: ListScope<Lang, LetterData>.() -> Unit) {
        ListScope<Lang, LetterData>().apply(create)
            .let { Element.OutlineContent.ParagraphContent.ItemList(it.elements, type = Listetype.PUNKTLISTE) }
            .let { Content(it) }
            .also { addParagraphContent(it) }
    }

    fun numberedList(create: ListScope<Lang, LetterData>.() -> Unit) {
        ListScope<Lang, LetterData>().apply(create)
            .let { Element.OutlineContent.ParagraphContent.ItemList(it.elements, type = Listetype.NUMMERERT_LISTE) }
            .let { Content(it) }
            .also { addParagraphContent(it) }
    }

    fun table(
        header: TableHeaderScope<Lang, LetterData>.() -> Unit,
        init: TableScope<Lang, LetterData>.() -> Unit
    ) {
        val colSpec = TableHeaderScope<Lang, LetterData>().apply(header).elements

        Element.OutlineContent.ParagraphContent.Table(
            rows = TableScope<Lang, LetterData>(colSpec).apply(init).elements,
            header = Element.OutlineContent.ParagraphContent.Table.Header(colSpec)
        ).let { Content(it) }
            .also { addParagraphContent(it) }
    }

}
