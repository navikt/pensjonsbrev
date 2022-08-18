package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.LanguageSupport

@LetterTemplateMarker
class TableScope<Lang : LanguageSupport, LetterData : Any>(private val colSpec: List<Element.Table.ColumnSpec<Lang>>) :
    ControlStructureScopeBase<Lang, LetterData, TableScope<Lang,LetterData>>(){
    fun row(
        init: TableRowScope<Lang, LetterData>.() -> Unit
    ) {
        children.add(Element.Table.Row(TableRowScope<Lang, LetterData>().apply(init).children, colSpec))
    }

    override fun scopeFactory(): TableScope<Lang, LetterData> = TableScope(colSpec)
}


@LetterTemplateMarker
open class TableRowScope<Lang : LanguageSupport, LetterData : Any>(val children: MutableList<Element.Table.Cell<Lang>> = mutableListOf()) :
    TemplateGlobalScope<LetterData>() {
    fun cell(init: TextOnlyScope<Lang, LetterData>.() -> Unit) {
        children.add(
            Element.Table.Cell(
                TextOnlyScope<Lang, LetterData>().apply(init).children
            )
        )
    }
}

@LetterTemplateMarker
open class TableHeaderScope<Lang : LanguageSupport, LetterData : Any>(
    val children: MutableList<Element.Table.ColumnSpec<Lang>> = mutableListOf()
) :
    TemplateGlobalScope<LetterData>() {
    fun column(
        columnSpan: Int = 1,
        alignment: Element.Table.ColumnAlignment = Element.Table.ColumnAlignment.LEFT,
        init: TextOnlyScope<Lang, LetterData>.() -> Unit,
    ) {

        children.add(
            Element.Table.ColumnSpec(
                Element.Table.Cell(TextOnlyScope<Lang, LetterData>().apply(init).children),
                alignment,
                columnSpan
            )
        )
    }
}