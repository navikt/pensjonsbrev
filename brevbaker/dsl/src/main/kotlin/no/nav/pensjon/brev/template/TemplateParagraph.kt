package no.nav.pensjon.brev.template.dsl

import no.nav.brev.Listetype
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.ContentOrControlStructure.Content


@LetterTemplateMarker
class ParagraphOnlyScope<Lang : LanguageSupport, LetterData : Any> internal constructor(): ParagraphScope<Lang, LetterData>, ControlStructureScope<Lang, LetterData, Element.OutlineContent.ParagraphContent<Lang>, ParagraphOnlyScope<Lang, LetterData>> {
    @BrevbakerDSLInternal override val elements: List<ParagraphContentElement<Lang>> field = mutableListOf<ParagraphContentElement<Lang>>()
    @BrevbakerDSLInternal override fun scopeFactory(): ParagraphOnlyScope<Lang, LetterData> = ParagraphOnlyScope()

    @BrevbakerDSLInternal
    override fun addControlStructure(e: ParagraphContentElement<Lang>) {
        elements.add(e)
    }

    @BrevbakerDSLInternal
    override fun addParagraphContent(e: ParagraphContentElement<Lang>) {
        elements.add(e)
    }

    @BrevbakerDSLInternal
    override fun addTextContentBaseLanguages(e: TextElement<BaseLanguages>) {
        // Safe because we know that a template that support BaseLanguages will support Lang
        @Suppress("UNCHECKED_CAST")
        elements.add(e as TextElement<Lang>)
    }

    @BrevbakerDSLInternal
    override fun addTextContent(e: TextElement<Lang>) {
        elements.add(e)
    }

    override fun newline() {
        addTextContent(Content(Element.OutlineContent.ParagraphContent.Text.NewLine<Lang>(elements.size)))
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

    fun formText(size: Element.OutlineContent.ParagraphContent.Form.Text.Size, prompt: PlainTextOnlyScope<Lang, LetterData>.() -> Unit, vspace: Boolean = true) {
        PlainTextOnlyScope<Lang, LetterData>().apply(prompt).elements
            .map { Element.OutlineContent.ParagraphContent.Form.Text(it, size, vspace) }
            .map { Content(it) }
            .forEach { addParagraphContent(it) }
    }

    fun formChoice(
        prompt: PlainTextOnlyScope<Lang, LetterData>.() -> Unit,
        vspace: Boolean = true,
        init: TemplateFormChoiceScope<Lang, LetterData>.() -> Unit
    ) {
        TemplateFormChoiceScope<Lang, LetterData>().apply(init)
            .let { choice ->
                PlainTextOnlyScope<Lang, LetterData>().apply(prompt).elements
                    .map { Element.OutlineContent.ParagraphContent.Form.MultipleChoice(it, choice.choices, vspace) }
                    .map { Content(it) }
                    .forEach { addParagraphContent(it) }
            }
    }
}
