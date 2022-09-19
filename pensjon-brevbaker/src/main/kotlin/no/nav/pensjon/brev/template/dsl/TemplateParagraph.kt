package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*


@LetterTemplateMarker
class ParagraphOnlyScope<Lang : LanguageSupport, LetterData : Any> : ParagraphScope<Lang, LetterData>, ControlStructureScope<Lang, LetterData, Element.OutlineContent.ParagraphContent<Lang>, ParagraphOnlyScope<Lang, LetterData>> {
    private val children = mutableListOf<ParagraphContentElement<Lang>>()
    override val elements: List<ParagraphContentElement<Lang>>
        get() = children

    override fun scopeFactory(): ParagraphOnlyScope<Lang, LetterData> = ParagraphOnlyScope()

    override fun addControlStructure(e: ParagraphContentElement<Lang>) {
        children.add(e)
    }

    override fun addParagraphContent(e: ParagraphContentElement<Lang>) {
        children.add(e)
    }

    override fun addTextContent(e: TextElement<Lang>) {
        children.add(e)
    }

    fun includePhrase(phrase: ParagraphPhrase<out Lang>) {
        phrase.apply(this)
    }
}

interface ParagraphScope<Lang : LanguageSupport, LetterData : Any> : TextScope<Lang, LetterData> {
    fun addParagraphContent(e: ParagraphContentElement<Lang>)

    fun list(create: ListScope<Lang, LetterData>.() -> Unit) {
        ListScope<Lang, LetterData>().apply(create)
            .let { Element.OutlineContent.ParagraphContent.ItemList(it.elements) }
            .let { ContentOrControlStructure.Content(it) }
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
        ).let { ContentOrControlStructure.Content(it) }
            .also { addParagraphContent(it) }
    }

    fun formText(size: Element.OutlineContent.ParagraphContent.Form.Text.Size, prompt: TextElement<Lang>, vspace: Boolean = true) {
        Element.OutlineContent.ParagraphContent.Form.Text(prompt, size, vspace)
            .let { ContentOrControlStructure.Content(it) }
            .also { addParagraphContent(it) }
    }

    fun formChoice(
        prompt: TextElement<Lang>,
        vspace: Boolean = true,
        init: TemplateFormChoiceScope<Lang, LetterData>.() -> Unit
    ) {
        TemplateFormChoiceScope<Lang, LetterData>().apply(init)
            .let { Element.OutlineContent.ParagraphContent.Form.MultipleChoice(prompt, it.choices, vspace) }
            .let { ContentOrControlStructure.Content(it) }
            .also { addParagraphContent(it) }
    }
}
