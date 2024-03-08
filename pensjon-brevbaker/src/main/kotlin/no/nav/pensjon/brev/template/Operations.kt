package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brevbaker.api.model.IntValue
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import kotlin.math.absoluteValue

abstract class Operation {
    // Since most operations don't have fields, and hence can't be data classes,
    // we override equals+hashCode to compare by class.
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String = "${this::class.simpleName}"
}

sealed class UnaryOperation<In, out Out> : Operation() {
    abstract fun apply(input: In): Out

    object ToString : UnaryOperation<Any, String>() {
        override fun apply(input: Any): String = input.toString()
    }

    object SizeOf : UnaryOperation<Collection<*>, Int>(){
        override fun apply(input: Collection<*>): Int = input.size
    }

    object AbsoluteValue : UnaryOperation<Int, Int>(){
        override fun apply(input: Int): Int = input.absoluteValue
    }

    object AbsoluteValueKroner : UnaryOperation<Kroner, Kroner>(){
        override fun apply(input: Kroner): Kroner = Kroner(input.value.absoluteValue)
    }

    object FormatPhoneNumber : UnaryOperation<Telefonnummer, String>() {
        override fun apply(input: Telefonnummer): String = input.format()
    }

    data class Select<In: Any, Out>(val selector: TemplateModelSelector<In, Out>) : UnaryOperation<In, Out>() {
        override fun apply(input: In): Out = selector.selector(input)
    }

    data class SafeCall<In: Any, Out>(val selector: TemplateModelSelector<In, Out>) : UnaryOperation<In?, Out?>() {
        override fun apply(input: In?): Out? = input?.let { selector.selector(it) }
    }

    // TODO: Should be a binary operation
    data class IfNull<In>(val then: In) : UnaryOperation<In?, In>() {
        override fun apply(input: In?): In = input ?: then
    }

    object Not : UnaryOperation<Boolean, Boolean>() {
        override fun apply(input: Boolean): Boolean = input.not()
    }

    data class MapCollection<In, Out>(val mapper: UnaryOperation<In, Out>): UnaryOperation<Collection<In>, Collection<Out>>() {
        override fun apply(input: Collection<In>): Collection<Out> = input.map { mapper.apply(it) }
    }

    object IsEmpty : UnaryOperation<Collection<*>, Boolean>() {
        override fun apply(input: Collection<*>): Boolean = input.isEmpty()
    }
}

abstract class BinaryOperation<in In1, in In2, out Out>(val doc: Documentation? = null) : Operation() {
    data class Documentation(val name: String, val syntax: Notation) {
        enum class Notation { PREFIX, INFIX, POSTFIX, FUNCTION }
    }

    abstract fun apply(first: In1, second: In2): Out

    class Equal<In> : BinaryOperation<In, In, Boolean>(Documentation("==", Documentation.Notation.INFIX)) {
        override fun apply(first: In, second: In): Boolean = first == second
    }

    class GreaterThan<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>(Documentation(">", Documentation.Notation.INFIX)) {
        override fun apply(first: T, second: T): Boolean = first > second
    }

    class GreaterThanOrEqual<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>(Documentation(">=", Documentation.Notation.INFIX)) {
        override fun apply(first: T, second: T): Boolean = first >= second
    }

    class LessThanOrEqual<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>(Documentation("<=", Documentation.Notation.INFIX)) {
        override fun apply(first: T, second: T): Boolean = first <= second
    }

    class LessThan<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>(Documentation("<", Documentation.Notation.INFIX)) {
        override fun apply(first: T, second: T): Boolean = first < second
    }

    object Or : BinaryOperation<Boolean, Boolean, Boolean>(Documentation("or", Documentation.Notation.INFIX)) {
        override fun apply(first: Boolean, second: Boolean): Boolean = first || second
    }

    object And : BinaryOperation<Boolean, Boolean, Boolean>(Documentation("and", Documentation.Notation.INFIX)) {
        override fun apply(first: Boolean, second: Boolean): Boolean = first && second
    }

    object Concat : BinaryOperation<String, String, String>() {
        override fun apply(first: String, second: String): String = first + second
    }

    class IntPlus<T : IntValue>(val constructor: (Int) -> T) : BinaryOperation<T, T, T>() {
        override fun apply(first: T, second: T): T = constructor(first.value + second.value)
    }

    data class MapCollection<In1, In2, Out>(val mapper: BinaryOperation<In1, In2, Out>): BinaryOperation<Collection<In1>, In2, Collection<Out>>() {
        override fun apply(first: Collection<In1>, second: In2): Collection<Out> = first.map { mapper.apply(it, second) }
    }

    class EnumInList<EnumType : Enum<*>> : BinaryOperation<EnumType, List<EnumType>, Boolean>() {
        override fun apply(first: EnumType, second: List<EnumType>): Boolean = second.contains(first)
    }

    class IfElse<Out> : BinaryOperation<Boolean, Pair<Out, Out>, Out>() {
        override fun apply(first: Boolean, second: Pair<Out, Out>): Out = if (first) second.first else second.second
    }

    class Tuple<Out> : BinaryOperation<Out, Out, Pair<Out, Out>>() {
        override fun apply(first: Out, second: Out): Pair<Out, Out> = first to second
    }

    data class Flip<In1, In2, Out>(val operation: BinaryOperation<In2, In1, Out>): BinaryOperation<In1, In2, Out>() {
        override fun apply(first: In1, second: In2): Out = operation.apply(second, first)
    }

}