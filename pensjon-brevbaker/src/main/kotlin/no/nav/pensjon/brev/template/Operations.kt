package no.nav.pensjon.brev.template

import no.nav.pensjon.brevbaker.api.model.IntValue
import no.nav.pensjon.brevbaker.api.model.Kroner
import kotlin.math.absoluteValue

abstract class Operation : StableHash {
    // Since most operations don't have fields, and hence can't be data classes,
    // we override equals+hashCode to compare by class.
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }
    override fun hashCode(): Int  = stableHashCode()
    override fun toString(): String = "${this::class.simpleName}"
}

sealed class UnaryOperation<In, out Out> : Operation() {
    abstract fun apply(input: In): Out

    object ToString : UnaryOperation<Any, String>() {
        override fun apply(input: Any): String = input.toString()
        override fun stableHashCode(): Int = "UnaryOperation.ToString".hashCode()
    }

    object SizeOf : UnaryOperation<Collection<*>, Int>(){
        override fun apply(input: Collection<*>): Int = input.size
        override fun stableHashCode(): Int = "UnaryOperation.SizeOf".hashCode()
    }

    object AbsoluteValue : UnaryOperation<Int, Int>(){
        override fun apply(input: Int): Int = input.absoluteValue
        override fun stableHashCode(): Int = "UnaryOperation.AbsoluteValue".hashCode()
    }

    object AbsoluteValueKroner : UnaryOperation<Kroner, Kroner>(){
        override fun apply(input: Kroner): Kroner = Kroner(input.value.absoluteValue)
        override fun stableHashCode(): Int = "UnaryOperation.AbsoluteValueKroner".hashCode()
    }

    class Select<In: Any, Out>(val selector: TemplateModelSelector<In, Out>) : UnaryOperation<In, Out>() {
        override fun apply(input: In): Out = selector.selector(input)
        override fun stableHashCode(): Int = 31 * "UnaryOperation.Select".hashCode() + selector.stableHashCode()
    }

    class SafeCall<In: Any, Out>(val selector: TemplateModelSelector<In, Out>) : UnaryOperation<In?, Out?>() {
        override fun apply(input: In?): Out? = input?.let { selector.selector(it) }
        override fun stableHashCode(): Int = 31 * "UnaryOperation.SafeCall".hashCode() + selector.stableHashCode()
    }

    object Not : UnaryOperation<Boolean, Boolean>() {
        override fun apply(input: Boolean): Boolean = input.not()
        override fun stableHashCode(): Int = "UnaryOperation.Not".hashCode()
    }

    data class MapCollection<In, Out>(val mapper: UnaryOperation<In, Out>): UnaryOperation<Collection<In>, Collection<Out>>() {
        override fun apply(input: Collection<In>): Collection<Out> = input.map { mapper.apply(it) }
        override fun stableHashCode(): Int = hashCode()
    }

    object IsEmpty : UnaryOperation<Collection<*>, Boolean>() {
        override fun apply(input: Collection<*>): Boolean = input.isEmpty()
        override fun stableHashCode(): Int = "UnaryOperation.IsEmpty".hashCode()
    }
}

abstract class BinaryOperation<in In1, in In2, out Out>(val doc: Documentation? = null) : Operation() {
    data class Documentation(val name: String, val syntax: Notation) {
        enum class Notation { PREFIX, INFIX, POSTFIX, FUNCTION }
    }

    abstract fun apply(first: In1, second: In2): Out

    class Equal<In> : BinaryOperation<In, In, Boolean>(Documentation("==", Documentation.Notation.INFIX)) {
        override fun apply(first: In, second: In): Boolean = first == second
        override fun stableHashCode(): Int = "BinaryOperation.Equal".hashCode()
    }

    class GreaterThan<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>(Documentation(">", Documentation.Notation.INFIX)) {
        override fun apply(first: T, second: T): Boolean = first > second
        override fun stableHashCode(): Int = "BinaryOperation.GreaterThan".hashCode()
    }

    class GreaterThanOrEqual<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>(Documentation(">=", Documentation.Notation.INFIX)) {
        override fun apply(first: T, second: T): Boolean = first >= second
        override fun stableHashCode(): Int = "BinaryOperation.GreaterThanOrEqual".hashCode()
    }

    class LessThanOrEqual<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>(Documentation("<=", Documentation.Notation.INFIX)) {
        override fun apply(first: T, second: T): Boolean = first <= second
        override fun stableHashCode(): Int = "BinaryOperation.LessThanOrEqual".hashCode()
    }

    class LessThan<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>(Documentation("<", Documentation.Notation.INFIX)) {
        override fun apply(first: T, second: T): Boolean = first < second
        override fun stableHashCode(): Int = "BinaryOperation.LessThan".hashCode()
    }

    object Or : BinaryOperation<Boolean, Boolean, Boolean>(Documentation("or", Documentation.Notation.INFIX)) {
        override fun apply(first: Boolean, second: Boolean): Boolean = first || second
        override fun stableHashCode(): Int = "BinaryOperation.Or".hashCode()
    }

    object And : BinaryOperation<Boolean, Boolean, Boolean>(Documentation("and", Documentation.Notation.INFIX)) {
        override fun apply(first: Boolean, second: Boolean): Boolean = first && second
        override fun stableHashCode(): Int = "BinaryOperation.And".hashCode()
    }

    object Concat : BinaryOperation<String, String, String>() {
        override fun apply(first: String, second: String): String = first + second
        override fun stableHashCode(): Int = "BinaryOperation.Concat".hashCode()
    }

    class IntPlus<T : IntValue>(val constructor: (Int) -> T) : BinaryOperation<T, T, T>() {
        override fun apply(first: T, second: T): T = constructor(first.value + second.value)
        override fun stableHashCode(): Int = "BinaryOperation.IntPlus".hashCode()
    }

    class IfNull<T : Any> : BinaryOperation<T?, T, T>(Documentation("?:", Documentation.Notation.INFIX)) {
        override fun apply(first: T?, second: T): T = first ?: second
        override fun stableHashCode(): Int = "BinaryOperation.IfNull".hashCode()
    }

    data class MapCollection<In1, In2, Out>(val mapper: BinaryOperation<In1, In2, Out>): BinaryOperation<Collection<In1>, In2, Collection<Out>>() {
        override fun apply(first: Collection<In1>, second: In2): Collection<Out> = first.map { mapper.apply(it, second) }
        override fun stableHashCode(): Int = hashCode()
    }

    class EnumInList<EnumType : Enum<*>> : BinaryOperation<EnumType, List<EnumType>, Boolean>() {
        override fun apply(first: EnumType, second: List<EnumType>): Boolean = second.contains(first)
        override fun stableHashCode(): Int = "BinaryOperation.EnumInList".hashCode()
    }

    class IfElse<Out> : BinaryOperation<Boolean, Pair<Out, Out>, Out>() {
        override fun apply(first: Boolean, second: Pair<Out, Out>): Out = if (first) second.first else second.second
        override fun stableHashCode(): Int = "BinaryOperation.IfElse".hashCode()
    }

    class Tuple<Out> : BinaryOperation<Out, Out, Pair<Out, Out>>() {
        override fun apply(first: Out, second: Out): Pair<Out, Out> = first to second
        override fun stableHashCode(): Int = "BinaryOperation.Tuple".hashCode()
    }

    data class Flip<In1, In2, Out>(val operation: BinaryOperation<In2, In1, Out>): BinaryOperation<In1, In2, Out>() {
        override fun apply(first: In1, second: In2): Out = operation.apply(second, first)
        override fun stableHashCode(): Int = hashCode()
    }

}