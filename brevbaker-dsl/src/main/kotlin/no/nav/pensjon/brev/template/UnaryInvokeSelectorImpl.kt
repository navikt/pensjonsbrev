package no.nav.pensjon.brev.template

import jdk.incubator.vector.VectorOperators.Unary
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

class UnaryOperationSelectorSelect<In : Any, Out>(override val selector: TemplateModelSelector<In, Out>) : UnaryOperation.Select<In, Out>, AbstractOperation() {
    private val impl = UnaryOperationImpl.Select(selector)
    override fun apply(input: In): Out = impl.apply(input)
    override fun stableHashCode(): Int = impl.stableHashCode()
    override fun hashCode() = impl.hashCode()
    override fun equals(other: Any?) = impl.equals(other)
    override fun toString() = impl.toString()
}

class UnaryOperationSelectorSafeCall<In : Any, Out>(override val selector: TemplateModelSelector<In, Out>) : UnaryOperation.SafeCall<In, Out>, AbstractOperation() {
    private val impl = UnaryOperationImpl.SafeCall(selector)
    override fun apply(input: In?) = impl.apply(input)
    override fun stableHashCode(): Int = impl.stableHashCode()
    override fun hashCode() = impl.hashCode()
    override fun equals(other: Any?) = impl.equals(other)
    override fun toString() = impl.toString()
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