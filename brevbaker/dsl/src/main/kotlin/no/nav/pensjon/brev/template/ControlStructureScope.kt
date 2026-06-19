package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.notNull

sealed interface ControlStructureScope<Lang : LanguageSupport, LetterData : Any, C : Element<Lang>, Scope : ControlStructureScope<Lang, LetterData, C, Scope>> : TemplateGlobalScope<LetterData> {
    @BrevbakerDSLInternal fun scopeFactory(): Scope
    @BrevbakerDSLInternal fun addControlStructure(e: ContentOrControlStructure<Lang, C>)
    @BrevbakerDSLInternal val elements: List<ContentOrControlStructure<Lang, C>>

    fun showIf(predicate: Expression<Boolean>, showIf: Scope.() -> Unit): ShowElseScope<Lang, LetterData, C, Scope> =
        createElseScope { elseScope ->
            addControlStructure(
                ContentOrControlStructure.Conditional(
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
        createElseScope { elseScope ->
            addControlStructure(
                ContentOrControlStructure.Conditional(
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
        createElseScope { elseScope ->
            addControlStructure(
                ContentOrControlStructure.Conditional(
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

    fun <Item : Any> forEach(items: Expression<Collection<Item>>, body: Scope.(item: Expression<Item>) -> Unit) {
        val nextExpr = Expression.FromScope.Assigned<Item>(items.stableHashCode())
        addControlStructure(ContentOrControlStructure.ForEach(items, scopeFactory().apply { body(nextExpr) }.elements, nextExpr))
    }

    private fun createElseScope(block: (elseScope: ShowElseScope<Lang, LetterData, C, Scope>) -> Unit) =
        // For scopeFactory argumentet til `ShowElseScope` lager vi først et nytt scope, slik at ShowElseScope ikke forurenses med det som skjer i `block`-lambda invokasjonen i `also`.
        ShowElseScope(scopeFactory()::scopeFactory).also(block)
}