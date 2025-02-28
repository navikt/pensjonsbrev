package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.notNull

interface ControlStructureScope<Lang : LanguageSupport, LetterData : Any, C : Element<Lang>, Scope : ControlStructureScope<Lang, LetterData, C, Scope>> : TemplateGlobalScope<LetterData> {
    fun scopeFactory(): Scope
    fun addControlStructure(e: ContentOrControlStructure<Lang, C>)
    val elements: List<ContentOrControlStructure<Lang, C>>

    fun showIf(predicate: Expression<Boolean>, showIf: Scope.() -> Unit): ShowElseScope<Lang, LetterData, C, Scope> =
        ShowElseScope(::scopeFactory).also { elseScope ->
            addControlStructure(
                ContentOrControlStructureImpl.ConditionalImpl(
                    predicate,
                    scopeFactory().apply(showIf).elements,
                    elseScope.scope.elements,
                )
            )
        }

    fun <E1 : Any> ifNotNull(
        expr1: Expression<E1?>,
        scope: Scope.(Expression<E1>) -> Unit
    ): ShowElseScope<Lang, LetterData, C, Scope> =
        ShowElseScope(::scopeFactory).also { elseScope ->
            addControlStructure(
                ContentOrControlStructureImpl.ConditionalImpl(
                    expr1.notNull(),
                    scopeFactory().apply {
                        // Følgende er en trygg cast fordi `elements` blir kun brukt om `expr1.notNull()` evaluerer til true.
                        @Suppress("UNCHECKED_CAST")
                        scope(this, expr1 as Expression<E1>)
                    }.elements,
                    elseScope.scope.elements,
                )
            )
        }

    fun <E1 : Any, E2 : Any> ifNotNull(
        expr1: Expression<E1?>,
        expr2: Expression<E2?>,
        scope: Scope.(Expression<E1>, Expression<E2>) -> Unit
    ): ShowElseScope<Lang, LetterData, C, Scope> =
        ShowElseScope(::scopeFactory).also { elseScope ->
            addControlStructure(
                ContentOrControlStructureImpl.ConditionalImpl(
                    expr1.notNull() and expr2.notNull(),
                    scopeFactory().apply {
                        // Følgende er en trygg cast fordi `elements` blir kun brukt om `expr1.notNull() and expr2.notNull()` evaluerer til true.
                        @Suppress("UNCHECKED_CAST")
                        scope(this, expr1 as Expression<E1>, expr2 as Expression<E2>)
                    }.elements,
                    elseScope.scope.elements,
                )
            )
        }

    fun <E1 : Any, E2 : Any, E3 : Any> ifNotNull(
        expr1: Expression<E1?>,
        expr2: Expression<E2?>,
        expr3: Expression<E3?>,
        scope: Scope.(Expression<E1>, Expression<E2>, Expression<E3>) -> Unit
    ): ShowElseScope<Lang, LetterData, C, Scope> =
        ShowElseScope(::scopeFactory).also { elseScope ->
            addControlStructure(
                ContentOrControlStructureImpl.ConditionalImpl(
                    expr1.notNull() and expr2.notNull() and expr3.notNull(),
                    scopeFactory().apply {
                        // Følgende er en trygg cast fordi `elements` blir kun brukt om `expr1.notNull() and expr2.notNull() and expr3.notNull()` evaluerer til true.
                        @Suppress("UNCHECKED_CAST")
                        scope(this, expr1 as Expression<E1>, expr2 as Expression<E2>, expr3 as Expression<E3>)
                    }.elements,
                    elseScope.scope.elements,
                )
            )
        }

    fun <Item : Any> forEach(items: Expression<Collection<Item>>, body: Scope.(item: Expression<Item>) -> Unit) {
        val nextExpr = ExpressionImpl.FromScopeImpl.AssignedImpl<Item>(items.stableHashCode())
        addControlStructure(ContentOrControlStructureImpl.ForEachImpl(items, scopeFactory().apply { body(nextExpr) }.elements, nextExpr))
    }
}