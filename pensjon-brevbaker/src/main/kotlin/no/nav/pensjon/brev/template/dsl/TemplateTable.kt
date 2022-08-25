package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*

@LetterTemplateMarker
class TableScope<Lang : LanguageSupport, LetterData : Any>(
    private val colSpec: List<Element.ParagraphContent.Table.ColumnSpec<Lang>>,
) : ControlStructureScope<Lang, LetterData, Element.ParagraphContent.Table.Row<Lang>, TableScope<Lang,LetterData>>{
    private val children = mutableListOf<TableRowElement<Lang>>()
    override val elements: List<TableRowElement<Lang>>
        get() = children

    override fun scopeFactory(): TableScope<Lang, LetterData> = TableScope(colSpec)

    override fun addControlStructure(e: TableRowElement<Lang>) {
        children.add(e)
    }

    fun row(create: TableRowScope<Lang, LetterData>.() -> Unit) {
        TableRowScope<Lang, LetterData>().apply(create)
            .let { Element.ParagraphContent.Table.Row(it.elements, colSpec) }
            .let { ContentOrControlStructure.Content(it) }
            .also { children.add(it) }
    }

}


@LetterTemplateMarker
class TableRowScope<Lang : LanguageSupport, LetterData : Any> : TemplateGlobalScope<LetterData> {
    private val children: MutableList<Element.ParagraphContent.Table.Cell<Lang>> = mutableListOf()
    val elements: List<Element.ParagraphContent.Table.Cell<Lang>>
        get() = children

    fun cell(init: TextOnlyScope<Lang, LetterData>.() -> Unit) {
        children.add(
            Element.ParagraphContent.Table.Cell(
                TextOnlyScope<Lang, LetterData>().apply(init).elements
            )
        )
    }
}

@LetterTemplateMarker
class TableHeaderScope<Lang : LanguageSupport, LetterData : Any> : TemplateGlobalScope<LetterData> {
    private val children: MutableList<Element.ParagraphContent.Table.ColumnSpec<Lang>> = mutableListOf()
    val elements: List<Element.ParagraphContent.Table.ColumnSpec<Lang>>
        get() = children

    fun column(
        columnSpan: Int = 1,
        alignment: Element.ParagraphContent.Table.ColumnAlignment = Element.ParagraphContent.Table.ColumnAlignment.LEFT,
        init: TextOnlyScope<Lang, LetterData>.() -> Unit,
    ) {

        children.add(
            Element.ParagraphContent.Table.ColumnSpec(
                Element.ParagraphContent.Table.Cell(TextOnlyScope<Lang, LetterData>().apply(init).elements),
                alignment,
                columnSpan
            )
        )
    }
}