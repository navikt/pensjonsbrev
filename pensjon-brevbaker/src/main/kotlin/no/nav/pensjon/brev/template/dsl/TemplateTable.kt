package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.LanguageSupport

//@LetterTemplateMarker
//class TableRootScope<Lang : LanguageSupport, LetterData : Any, Scope : TableRootScope<Lang, LetterData, Scope>>(
//    children: MutableList<Element<Lang>> = mutableListOf()
//) : TemplateManipulationScope<Lang, LetterData, TableRootScope<Lang,LetterData, Scope>>(children) {
//    override fun scopeFactory(): TableBaseScope<Lang, LetterData> = TableBaseScope()
//}

@LetterTemplateMarker
class TableBaseScope<Lang : LanguageSupport, LetterData : Any>
    : TemplateManipulationScope<Lang, LetterData, TableBaseScope<Lang,LetterData>>(){
    fun row(
        init: TableRowScope<Lang, LetterData>.() -> Unit
    ) {
        children.add(Element.Table.Row(TableRowScope<Lang, LetterData>().apply(init).children))
    }

    override fun scopeFactory(): TableBaseScope<Lang, LetterData> = TableBaseScope()
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