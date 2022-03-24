package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.notNull


@LetterTemplateMarker
open class ListRootScope<Lang : LanguageSupport, LetterData : Any>
    : ListBaseScope<Lang, LetterData>() {
    fun showIf(
        predicate: Expression<Boolean>,
        showIf: ListBaseScope<Lang, LetterData>.() -> Unit
    ) {
        ListBaseScope<Lang, LetterData>().apply(showIf).also { builder ->
            items.addAll(builder.items.map {
                it.copy(condition = predicate)
            })
        }
    }

    fun <E1 : Any> ifNotNull(
        expr1: Expression<E1?>,
        scope: ListBaseScope<Lang, LetterData>.(Expression<E1>) -> Unit
    ) {

        items.addAll(
            ListBaseScope<Lang, LetterData>().apply {
                // Følgende er en trygg cast fordi `children` blir kun brukt om `expr1.notNull()` evaluerer til true.
                @Suppress("UNCHECKED_CAST")
                scope(this, expr1 as Expression<E1>)
            }.items.map { it.copy(condition = expr1.notNull()) }
        )
    }

    fun <E1 : Any, E2 : Any> ifNotNull(
        expr1: Expression<E1?>,
        expr2: Expression<E2?>,
        scope: ListBaseScope<Lang, LetterData>.(Expression<E1>, Expression<E2>) -> Unit
    ) {

        items.addAll(
            ListBaseScope<Lang, LetterData>().apply {
                // Følgende er en trygg cast fordi `children` blir kun brukt om `expr1.notNull()` evaluerer til true.
                @Suppress("UNCHECKED_CAST")
                scope(this, expr1 as Expression<E1>, expr2 as Expression<E2>)
            }.items.map { it.copy(condition = (expr1.notNull() and expr2.notNull())) }
        )
    }

    fun <E1 : Any, E2 : Any, E3 : Any> ifNotNull(
        expr1: Expression<E1?>,
        expr2: Expression<E2?>,
        expr3: Expression<E3?>,
        scope: ListBaseScope<Lang, LetterData>.(Expression<E1>, Expression<E2>, Expression<E3>) -> Unit
    ) {

        items.addAll(
            ListBaseScope<Lang, LetterData>().apply {
                // Følgende er en trygg cast fordi `children` blir kun brukt om `expr1.notNull()` evaluerer til true.
                @Suppress("UNCHECKED_CAST")
                scope(this, expr1 as Expression<E1>, expr2 as Expression<E2>, expr3 as Expression<E3>)
            }.items.map { it.copy(condition = (expr1.notNull() and expr2.notNull() and expr2.notNull())) }
        )
    }

}

@LetterTemplateMarker
open class ListBaseScope<Lang : LanguageSupport, LetterData : Any>(
    val items: MutableList<Element.ItemList.Item<Lang>> = mutableListOf()
) : TemplateGlobalScope<LetterData>() {
    fun item(
        init: TextOnlyScope<Lang, LetterData>.() -> Unit
    ) {
        items.add(Element.ItemList.Item(TextOnlyScope<Lang, LetterData>().apply(init).children))
    }
}

