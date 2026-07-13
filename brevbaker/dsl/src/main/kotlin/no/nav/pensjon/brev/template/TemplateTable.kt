package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*

@LetterTemplateMarker
class TableScope<Lang : LanguageSupport, LetterData : Any> internal constructor(
    private val colSpec: List<Element.OutlineContent.ParagraphContent.Table.ColumnSpec<Lang>>,
) : ControlStructureScope<Lang, LetterData, Element.OutlineContent.ParagraphContent.Table.Row<Lang>, TableScope<Lang,LetterData>>{
    @BrevbakerDSLInternal override val elements: List<TableRowElement<Lang>> field = mutableListOf<TableRowElement<Lang>>()
    @BrevbakerDSLInternal override fun scopeFactory(): TableScope<Lang, LetterData> = TableScope(colSpec)

    @BrevbakerDSLInternal
    override fun addControlStructure(e: TableRowElement<Lang>) {
        elements.add(e)
    }

    fun row(create: TableRowScope<Lang, LetterData>.() -> Unit) {
        TableRowScope<Lang, LetterData>().apply(create)
            .let { Element.OutlineContent.ParagraphContent.Table.Row(it.elements, colSpec) }
            .let { ContentOrControlStructure.Content(it) }
            .also { elements.add(it) }
    }

}


@LetterTemplateMarker
class TableRowScope<Lang : LanguageSupport, LetterData : Any> internal constructor(): TemplateGlobalScope<LetterData> {
    @BrevbakerDSLInternal
    val elements: List<Element.OutlineContent.ParagraphContent.Table.Cell<Lang>>
        field: MutableList<Element.OutlineContent.ParagraphContent.Table.Cell<Lang>> = mutableListOf()

    fun cell(init: TextOnlyScope<Lang, LetterData>.() -> Unit) {
        elements.add(
            Element.OutlineContent.ParagraphContent.Table.Cell(
                TextOnlyScope<Lang, LetterData>().apply(init).elements
            )
        )
    }
}

@LetterTemplateMarker
class TableHeaderScope<Lang : LanguageSupport, LetterData : Any> internal constructor(): TemplateGlobalScope<LetterData> {
    @BrevbakerDSLInternal
    val elements: List<Element.OutlineContent.ParagraphContent.Table.ColumnSpec<Lang>>
        field: MutableList<Element.OutlineContent.ParagraphContent.Table.ColumnSpec<Lang>> = mutableListOf()

    fun column(
        columnSpan: Int = 1,
        alignment: Element.OutlineContent.ParagraphContent.Table.ColumnAlignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT,
        init: PlainTextOnlyScope<Lang, LetterData>.() -> Unit,
    ) {
        elements.add(
            Element.OutlineContent.ParagraphContent.Table.ColumnSpec(
                Element.OutlineContent.ParagraphContent.Table.Cell(PlainTextOnlyScope<Lang, LetterData>().apply(init).elements),
                alignment,
                columnSpan
            )
        )
    }
}