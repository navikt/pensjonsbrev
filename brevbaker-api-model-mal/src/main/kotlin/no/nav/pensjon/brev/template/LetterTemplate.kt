package no.nav.pensjon.brev.template

import no.nav.pensjon.brevbaker.api.model.ElementTags
import no.nav.pensjon.brevbaker.api.model.IntValue
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import java.time.LocalDate


sealed class Expression<out Out> : StableHash {

    abstract fun eval(scope: ExpressionScope<*>): Out

    data class Literal<out Out>(val value: Out, val tags: Set<ElementTags> = emptySet()) : Expression<Out>() {
        override fun eval(scope: ExpressionScope<*>): Out = value
        override fun stableHashCode(): Int = stableHash(value).stableHashCode()

        private fun stableHash(v: Any?): StableHash =
            when (v) {
                is StableHash -> v
                is Enum<*> -> StableHash.of(v)
                is String -> StableHash.of(v)
                is Number -> StableHash.of(v)
                is IntValue -> StableHash.of(v.value)
                is Telefonnummer -> StableHash.of(v.value)
                is Boolean -> StableHash.of(v)
                is Collection<*> -> StableHash.of(v.map { stableHash(it) })
                is Pair<*, *> -> StableHash.of(stableHash(v.first), stableHash(v.second))
                is Unit -> StableHash.of("kotlin.Unit")
                is LocalDate -> StableHash.of(v)
                null -> StableHash.of(null)
                else -> throw IllegalArgumentException("Unable to calculate stableHashCode for type ${v::class.java}")
            }
    }

    sealed class FromScope<out Out> : Expression<Out>() {
        object Felles : FromScope<no.nav.pensjon.brevbaker.api.model.Felles>() {
            override fun eval(scope: ExpressionScope<*>) = scope.felles
            override fun stableHashCode(): Int = "FromScope.Felles".hashCode()
        }

        object Language : FromScope<no.nav.pensjon.brev.template.Language>() {
            override fun eval(scope: ExpressionScope<*>) = scope.language
            override fun stableHashCode(): Int = "FromScope.Language".hashCode()
        }

        class Argument<out Out> : FromScope<Out>() {
            @Suppress("UNCHECKED_CAST")
            override fun eval(scope: ExpressionScope<*>) = scope.argument as Out
            override fun equals(other: Any?): Boolean = other is Argument<*>
            override fun hashCode(): Int = javaClass.hashCode()
            override fun stableHashCode(): Int = "FromScope.Argument".hashCode()
        }

        data class Assigned<out Out>(val id: Int) : FromScope<Out>() {
            override fun eval(scope: ExpressionScope<*>): Out =
                if (scope is ExpressionScope.WithAssignment<*, *>) {
                    @Suppress("UNCHECKED_CAST")
                    (scope as ExpressionScope.WithAssignment<*, Out>).lookup(this)
                } else {
                    throw InvalidScopeTypeException("Requires scope to be ${this::class.qualifiedName}, but was: ${scope::class.qualifiedName}")
                }

            override fun stableHashCode() = hashCode()
        }
    }

    data class UnaryInvoke<In, Out>(
        val value: Expression<In>,
        val operation: UnaryOperation<In, Out>,
    ) : Expression<Out>(), StableHash by StableHash.of(value, operation) {
        override fun eval(scope: ExpressionScope<*>): Out = operation.apply(value.eval(scope))
    }

    data class BinaryInvoke<In1, In2, out Out>(
        val first: Expression<In1>,
        val second: Expression<In2>,
        val operation: BinaryOperation<In1, In2, Out>
    ) : Expression<Out>(), StableHash by StableHash.of(first, second, operation) {
        override fun eval(scope: ExpressionScope<*>): Out = operation.apply(first.eval(scope), second.eval(scope))
    }

    final override fun toString(): String {
        throw PreventToStringForExpressionException()
    }
}

typealias StringExpression = Expression<String>