package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LanguageSupport

@LetterTemplateMarker
open class TableRootScope<Lang : LanguageSupport, LetterData : Any>
    : TableBaseScope<Lang, LetterData>() {

    fun title(
        init: TextOnlyScope<Lang, LetterData>.() -> Unit
    ) {
        title = TextOnlyScope<Lang, LetterData>().apply(init).children
    }

    fun showIf(
        predicate: Expression<Boolean>,
        showIf: TableBaseScope<Lang, LetterData>.() -> Unit
    ) {
        TableBaseScope<Lang, LetterData>().apply(showIf).also { builder ->
            children.addAll(builder.children.map {
                it.copy(condition = predicate)
            })

            columnHeaders.addAll(builder.columnHeaders.map {
                it.copy(condition = predicate)
            })
        }
    }
}

@LetterTemplateMarker
open class TableBaseScope<Lang : LanguageSupport, LetterData : Any>(
    var title: List<Element<Lang>>? = null,
    val columnHeaders: MutableList<Element.Table.Row<Lang>> = mutableListOf(),
    val children: MutableList<Element.Table.Row<Lang>> = mutableListOf()
) : TemplateGlobalScope<LetterData>() {

    fun columnHeaderRow(
        init: TableRowScope<Lang, LetterData>.() -> Unit
    ) {
        columnHeaders.add(Element.Table.Row(TableRowScope<Lang, LetterData>().apply(init).children))
    }

    fun row(
        init: TableRowScope<Lang, LetterData>.() -> Unit
    ) {
        children.add(Element.Table.Row(TableRowScope<Lang, LetterData>().apply(init).children))
    }
}


@LetterTemplateMarker
open class TableRowScope<Lang : LanguageSupport, LetterData : Any>(val children: MutableList<Element.Table.Cell<Lang>> = mutableListOf()) :
    TemplateGlobalScope<LetterData>() {
    fun cell(cellColumns: Int = 1, init: TextOnlyScope<Lang, LetterData>.() -> Unit) {
        children.add(
            Element.Table.Cell(
                TextOnlyScope<Lang, LetterData>().apply(init).children,
                cellColumns
            )
        )
    }
}