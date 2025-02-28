package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.Expression.FromScope
import no.nav.pensjon.brev.template.ExpressionImpl.FromScopeImpl.ArgumentImpl

data class UnaryInvokeSelectorImpl<In, Out>(
    override val value: Expression<In>,
    override val operation: UnaryOperation<In, Out>,
) : Expression.UnaryInvoke<In, Out>, StableHash by StableHash.of(value, operation) {
    override fun eval(scope: ExpressionScope<*>): Out = operation.apply(value.eval(scope))

    override fun toString(): String {
        throw PreventToStringForExpressionException()
    }
}

class ArgumentSelectorImpl<out Out> : FromScope.Argument<Out> {
    @Suppress("UNCHECKED_CAST")
    override fun eval(scope: ExpressionScope<*>) = scope.argument as Out
    override fun equals(other: Any?): Boolean = other is ArgumentImpl<*>
    override fun hashCode(): Int = javaClass.hashCode()
    override fun stableHashCode(): Int = "FromScope.Argument".hashCode()

    override fun toString(): String {
        throw PreventToStringForExpressionException()
    }
}