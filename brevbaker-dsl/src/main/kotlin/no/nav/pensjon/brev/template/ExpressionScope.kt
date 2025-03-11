package no.nav.pensjon.brev.template

import no.nav.pensjon.brevbaker.api.model.Felles

open class ExpressionScope<Argument : Any>(val argument: Argument, val felles: Felles, val language: Language) {

    class WithAssignment<Argument: Any, Var> internal constructor(
        val value: Var,
        val expr: Expression.FromScope.Assigned<Var>,
        private val parent: ExpressionScope<Argument>
    ): ExpressionScope<Argument>(parent.argument, parent.felles, parent.language) {
        fun lookup(expr: Expression.FromScope.Assigned<Var>): Var =
            // Uses referential equality since nested ForEach over the same collection-expression will be equal.
            if (expr === this.expr) {
                value
            } else if (parent is WithAssignment<*, *>) {
                @Suppress("UNCHECKED_CAST")
                (parent as WithAssignment<*, Var>).lookup(expr)
            } else {
                throw MissingScopeForNextItemEvaluationException("Could not find scope matching: $expr")
            }
    }

    fun <Var> assign(value: Var, to: Expression.FromScope.Assigned<Var>): WithAssignment<Argument, Var> =
        WithAssignment(value, to, this)

}