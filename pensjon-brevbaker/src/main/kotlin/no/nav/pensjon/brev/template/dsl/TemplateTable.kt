package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LanguageSupport

@LetterTemplateMarker
open class TableRootScope<Lang : LanguageSupport, LetterData : Any>
    : TableBaseScope<Lang, LetterData>() {

    fun showIf(
        predicate: Expression<Boolean>,
        showIf: TableBaseScope<Lang, LetterData>.() -> Unit
    ) {
        TableBaseScope<Lang, LetterData>().apply(showIf).also { builder ->
            rows.addAll(builder.rows.map {
                it.copy(condition = predicate)
            })
        }
    }
}

@LetterTemplateMarker
open class TableBaseScope<Lang : LanguageSupport, LetterData : Any>(
    var columnHeader: Element.Table.Header<Lang>? = null,
    val rows: MutableList<Element.Table.Row<Lang>> = mutableListOf()
) : TemplateGlobalScope<LetterData>() {

    fun columnHeaderRow(
        init: TableHeaderScope<Lang, LetterData>.() -> Unit
    ) {
        val headerScope = TableHeaderScope<Lang, LetterData>().apply(init)
        columnHeader = Element.Table.Header(headerScope.children, headerScope.columnAlignment)
    }

    fun row(
        init: TableRowScope<Lang, LetterData>.() -> Unit
    ) {
        rows.add(Element.Table.Row(TableRowScope<Lang, LetterData>().apply(init).children))
    }
}


@LetterTemplateMarker
open class TableRowScope<Lang : LanguageSupport, LetterData : Any>(val children: MutableList<Element.Table.Cell<Lang>> = mutableListOf()) :
    TemplateGlobalScope<LetterData>() {
    fun cell(columns: Int = 1, init: TemplateTextOnlyScope<Lang, LetterData>.() -> Unit) {
        children.add(
            Element.Table.Cell(
                TemplateTextOnlyScope<Lang, LetterData>().apply(init).children,
                columns
            )
        )
    }
}

@LetterTemplateMarker
open class TableHeaderScope<Lang : LanguageSupport, LetterData : Any>(
    val children: MutableList<Element.Table.Cell<Lang>> = mutableListOf(),
    val columnAlignment: MutableList<Element.Table.ColumnAlignment> = mutableListOf()
) :
    TemplateGlobalScope<LetterData>() {
    fun cell(
        columns: Int = 1,
        alignment: Element.Table.ColumnAlignment = Element.Table.ColumnAlignment.LEFT,
        init: TemplateTextOnlyScope<Lang, LetterData>.() -> Unit
    ) {

        children.add(
            Element.Table.Cell(
                TemplateTextOnlyScope<Lang, LetterData>().apply(init).children,
                columns
            )
        )
        columnAlignment.add(alignment)
    }
}