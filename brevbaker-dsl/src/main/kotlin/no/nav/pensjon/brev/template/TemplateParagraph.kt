package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.ContentOrControlStructureImpl.ContentImpl


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

    override fun newline() {
        addTextContent(ContentImpl(ElementImpl.OutlineContentImpl.ParagraphContentImpl.TextImpl.NewLineImpl(children.size)))
    }

    fun includePhrase(phrase: ParagraphPhrase<out Lang>) {
        phrase.apply(this)
    }
}

interface ParagraphScope<Lang : LanguageSupport, LetterData : Any> : TextScope<Lang, LetterData> {
    fun addParagraphContent(e: ParagraphContentElement<Lang>)

    fun list(create: ListScope<Lang, LetterData>.() -> Unit) {
        ListScope<Lang, LetterData>().apply(create)
            .let { ElementImpl.OutlineContentImpl.ParagraphContentImpl.ItemListImpl(it.elements) }
            .let { ContentImpl(it) }
            .also { addParagraphContent(it) }
    }

    fun table(
        header: TableHeaderScope<Lang, LetterData>.() -> Unit,
        init: TableScope<Lang, LetterData>.() -> Unit
    ) {
        val colSpec = TableHeaderScope<Lang, LetterData>().apply(header).elements

        ElementImpl.OutlineContentImpl.ParagraphContentImpl.TableImpl(
            rows = TableScope<Lang, LetterData>(colSpec).apply(init).elements,
            header = ElementImpl.OutlineContentImpl.ParagraphContentImpl.TableImpl.HeaderImpl(colSpec)
        ).let { ContentImpl(it) }
            .also { addParagraphContent(it) }
    }

    fun formText(size: Element.OutlineContent.ParagraphContent.Form.Text.Size, prompt: TextElement<Lang>, vspace: Boolean = true) {
        ElementImpl.OutlineContentImpl.ParagraphContentImpl.FormImpl.TextImpl(prompt, size, vspace)
            .let { ContentImpl(it) }
            .also { addParagraphContent(it) }
    }

    fun formChoice(
        prompt: TextElement<Lang>,
        vspace: Boolean = true,
        init: TemplateFormChoiceScope<Lang, LetterData>.() -> Unit
    ) {
        TemplateFormChoiceScope<Lang, LetterData>().apply(init)
            .let { ElementImpl.OutlineContentImpl.ParagraphContentImpl.FormImpl.MultipleChoiceImpl(prompt, it.choices, vspace) }
            .let { ContentImpl(it) }
            .also { addParagraphContent(it) }
    }
}
