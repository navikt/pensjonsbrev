package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*

@LetterTemplateMarker
class TableScope<Lang : LanguageSupport, LetterData : Any> internal constructor(
    private val colSpec: List<Element.OutlineContent.ParagraphContent.Table.ColumnSpec<Lang>>,
) : ControlStructureScope<Lang, LetterData, Element.OutlineContent.ParagraphContent.Table.Row<Lang>, TableScope<Lang,LetterData>>{
    private val _elements = mutableListOf<TableRowElement<Lang>>()
    @BrevbakerDSLInternal override val elements: List<TableRowElement<Lang>> get() = _elements
    @BrevbakerDSLInternal override fun scopeFactory(): TableScope<Lang, LetterData> = TableScope(colSpec)

    @BrevbakerDSLInternal
    override fun addControlStructure(e: TableRowElement<Lang>) {
        _elements.add(e)
    }

    fun row(create: TableRowScope<Lang, LetterData>.() -> Unit) {
        TableRowScope<Lang, LetterData>().apply(create)
            .let { Element.OutlineContent.ParagraphContent.Table.Row(it.elements, colSpec) }
            .let { ContentOrControlStructure.Content(it) }
            .also { _elements.add(it) }
    }

}


@LetterTemplateMarker
class TableRowScope<Lang : LanguageSupport, LetterData : Any> internal constructor(): TemplateGlobalScope<LetterData> {
    private val _elements: MutableList<Element.OutlineContent.ParagraphContent.Table.Cell<Lang>> = mutableListOf()
    @BrevbakerDSLInternal val elements: List<Element.OutlineContent.ParagraphContent.Table.Cell<Lang>> get() = _elements

    fun cell(init: TextOnlyScope<Lang, LetterData>.() -> Unit) {
        _elements.add(
            Element.OutlineContent.ParagraphContent.Table.Cell(
                TextOnlyScope<Lang, LetterData>().apply(init).elements
            )
        )
    }
}

@LetterTemplateMarker
class TableHeaderScope<Lang : LanguageSupport, LetterData : Any> internal constructor(): TemplateGlobalScope<LetterData> {
    private val _elements: MutableList<Element.OutlineContent.ParagraphContent.Table.ColumnSpec<Lang>> = mutableListOf()
    @BrevbakerDSLInternal val elements: List<Element.OutlineContent.ParagraphContent.Table.ColumnSpec<Lang>> get() = _elements

    fun column(
        columnSpan: Int = 1,
        alignment: Element.OutlineContent.ParagraphContent.Table.ColumnAlignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT,
        init: PlainTextOnlyScope<Lang, LetterData>.() -> Unit,
    ) {

        _elements.add(
            Element.OutlineContent.ParagraphContent.Table.ColumnSpec(
                Element.OutlineContent.ParagraphContent.Table.Cell(PlainTextOnlyScope<Lang, LetterData>().apply(init).elements),
                alignment,
                columnSpan
            )
        )
    }
}