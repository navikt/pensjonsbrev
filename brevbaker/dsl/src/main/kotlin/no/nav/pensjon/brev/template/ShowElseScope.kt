package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.expression.notNull

class ShowElseScope<Lang : LanguageSupport, LetterData : Any, C : Element<Lang>, Scope : ControlStructureScope<Lang, LetterData, C, Scope>> internal constructor(
    private val scopeFactory: () -> Scope,
    private val parentScope: ControlStructureScope<Lang, LetterData, C, Scope>,
) {
    internal val scope: Scope = scopeFactory()

    infix fun orShow(body: Scope.() -> Unit) {
        scope.apply(body)
        parentScope.registerAddedContent(scope.elements)
    }

    fun orShowIf(
        predicate: Expression<Boolean>,
        body: Scope.() -> Unit
    ): ShowElseScope<Lang, LetterData, C, Scope> =
        ShowElseScope(scopeFactory, parentScope).also { showElse ->
            scope.addControlStructure(
                ContentOrControlStructure.Conditional(
                    predicate = predicate,
                    showIf = scopeFactory().apply(body).elements,
                    showElse = showElse.scope.elements
                )
            )
            parentScope.registerAddedContent(scope.elements)
        }

    fun <E1 : Any> orIfNotNull(
        expr1: Expression<E1?>,
        body: Scope.(Expression<E1>) -> Unit
    ): ShowElseScope<Lang, LetterData, C, Scope> =
        ShowElseScope(scopeFactory, parentScope).also { elseScope ->
            scope.addControlStructure(
                ContentOrControlStructure.Conditional(
                    expr1.notNull(),
                    scopeFactory().apply {
                        // Følgende er en trygg cast fordi `elements` blir kun brukt om `expr1.notNull()` evaluerer til true.
                        @Suppress("UNCHECKED_CAST")
                        body(this, expr1 as Expression<E1>)
                    }.elements,
                    elseScope.scope.elements,
                )
            )
            parentScope.registerAddedContent(scope.elements)
        }
}
