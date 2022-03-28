package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.notNull

abstract class TemplateManipulationScope<Lang : LanguageSupport, LetterData : Any, Scope : TemplateManipulationScope<Lang, LetterData, Scope>>(
    val children: MutableList<Element<Lang>> = mutableListOf()
) : TemplateGlobalScope<LetterData>() {
    protected abstract fun scopeFactory(): Scope
    fun showIf(predicate: Expression<Boolean>, showIf: Scope.() -> Unit): ShowElseScope<Lang, LetterData, Scope> =
        ShowElseScope(::scopeFactory).also { elseScope ->
            children.add(
                Element.Conditional(
                    predicate,
                    scopeFactory().apply(showIf).children,
                    elseScope.scope.children
                )
            )
        }

    fun <E1 : Any> ifNotNull(
        expr1: Expression<E1?>,
        scope: Scope.(Expression<E1>) -> Unit
    ) {
        children.add(
            Element.Conditional(expr1.notNull(), scopeFactory().apply {
                // Følgende er en trygg cast fordi `children` blir kun brukt om `expr1.notNull()` evaluerer til true.
                @Suppress("UNCHECKED_CAST")
                scope(this, expr1 as Expression<E1>)
            }.children, emptyList())
        )
    }

    fun <E1 : Any, E2 : Any> ifNotNull(
        expr1: Expression<E1?>,
        expr2: Expression<E2?>,
        scope: Scope.(Expression<E1>, Expression<E2>) -> Unit
    ) {
        children.add(
            Element.Conditional(expr1.notNull() and expr2.notNull(), scopeFactory().apply {
                // Følgende er en trygg cast fordi `children` blir kun brukt om `expr1.notNull() and expr2.notNull()` evaluerer til true.
                @Suppress("UNCHECKED_CAST")
                scope(this, expr1 as Expression<E1>, expr2 as Expression<E2>)
            }.children, emptyList())
        )
    }

    fun <E1 : Any, E2 : Any, E3 : Any> ifNotNull(
        expr1: Expression<E1?>,
        expr2: Expression<E2?>,
        expr3: Expression<E3?>,
        scope: Scope.(Expression<E1>, Expression<E2>, Expression<E3>) -> Unit
    ) {
        children.add(
            Element.Conditional(expr1.notNull() and expr2.notNull() and expr3.notNull(), scopeFactory().apply {
                // Følgende er en trygg cast fordi `children` blir kun brukt om `expr1.notNull() and expr2.notNull() and expr3.notNull()` evaluerer til true.
                @Suppress("UNCHECKED_CAST")
                scope(this, expr1 as Expression<E1>, expr2 as Expression<E2>, expr3 as Expression<E3>)
            }.children, emptyList())
        )
    }

    fun <Item : Any> forEach(items: Expression<List<Item>>, body: Scope.(item: Expression<Item>) -> Unit) {
        children.add(Element.ForEachView.create(items) { expr -> scopeFactory().apply { body(expr) }.children })
    }
}