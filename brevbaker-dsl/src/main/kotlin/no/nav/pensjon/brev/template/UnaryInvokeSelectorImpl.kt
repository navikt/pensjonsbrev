package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.Expression.FromScope
import no.nav.pensjon.brev.template.ExpressionImpl.FromScopeImpl.ArgumentImpl

// TODO: Vurder denne som vanleg class
data class UnaryInvokeSelectorImpl<In, Out>(
    override val value: Expression<In>,
    override val operation: UnaryOperation<In, Out>,
) : Expression.UnaryInvoke<In, Out>, StableHash by StableHash.of(value, operation) {

    private val impl = ExpressionImpl.UnaryInvokeImpl(value, operation)

    override fun eval(scope: ExpressionScope<*>): Out = impl.eval(scope)

    override fun toString() = impl.toString()

    override fun equals(other: Any?): Boolean = impl.equals(other)
    override fun hashCode(): Int = impl.hashCode()
    override fun stableHashCode(): Int = impl.stableHashCode()
}

class ArgumentSelectorImpl<out Out> : FromScope.Argument<Out> {

    private val impl = ArgumentImpl<Out>()

    @Suppress("UNCHECKED_CAST")
    override fun eval(scope: ExpressionScope<*>) = impl.eval(scope)
    override fun equals(other: Any?): Boolean = impl.equals(other)
    override fun hashCode(): Int = impl.hashCode()
    override fun stableHashCode(): Int = impl.stableHashCode()

    override fun toString() = impl.toString()
}