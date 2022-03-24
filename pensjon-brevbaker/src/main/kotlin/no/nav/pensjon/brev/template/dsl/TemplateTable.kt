package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.notNull

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

    fun <E1 : Any> ifNotNull(
        expr1: Expression<E1?>,
        scope: TableBaseScope<Lang, LetterData>.(Expression<E1>) -> Unit
    ) {

        rows.addAll(
            TableBaseScope<Lang, LetterData>().apply {
                // Følgende er en trygg cast fordi `rows` blir kun brukt om `expr1.notNull()` evaluerer til true.
                @Suppress("UNCHECKED_CAST")
                scope(this, expr1 as Expression<E1>)
            }.rows.map { it.copy(condition = expr1.notNull()) }
        )
    }

    fun <E1 : Any, E2 : Any> ifNotNull(
        expr1: Expression<E1?>,
        expr2: Expression<E2?>,
        scope: TableBaseScope<Lang, LetterData>.(Expression<E1>, Expression<E2>) -> Unit
    ) {

        rows.addAll(
            TableBaseScope<Lang, LetterData>().apply {
                // Følgende er en trygg cast fordi `rows` blir kun brukt om `expr1.notNull()` evaluerer til true.
                @Suppress("UNCHECKED_CAST")
                scope(this, expr1 as Expression<E1>, expr2 as Expression<E2>)
            }.rows.map { it.copy(condition = (expr1.notNull() and expr2.notNull())) }
        )
    }

    fun <E1 : Any, E2 : Any, E3 : Any> ifNotNull(
        expr1: Expression<E1?>,
        expr2: Expression<E2?>,
        expr3: Expression<E3?>,
        scope: TableBaseScope<Lang, LetterData>.(Expression<E1>, Expression<E2>, Expression<E3>) -> Unit
    ) {

        rows.addAll(
            TableBaseScope<Lang, LetterData>().apply {
                // Følgende er en trygg cast fordi `rows` blir kun brukt om `expr1.notNull()` evaluerer til true.
                @Suppress("UNCHECKED_CAST")
                scope(this, expr1 as Expression<E1>, expr2 as Expression<E2>, expr3 as Expression<E3>)
            }.rows.map { it.copy(condition = (expr1.notNull() and expr2.notNull() and expr2.notNull())) }
        )
    }
}

@LetterTemplateMarker
open class TableBaseScope<Lang : LanguageSupport, LetterData : Any>(
    val rows: MutableList<Element.Table.Row<Lang>> = mutableListOf()
) : TemplateGlobalScope<LetterData>() {
    fun row(
        init: TableRowScope<Lang, LetterData>.() -> Unit
    ) {
        rows.add(Element.Table.Row(TableRowScope<Lang, LetterData>().apply(init).children))
    }
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