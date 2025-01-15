package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.UnleashToggle
import no.nav.pensjon.brev.template.render.fulltNavn
import no.nav.pensjon.brevbaker.api.model.Bruker
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

    override fun hashCode(): Int = stableHashCode()
    override fun toString(): String = "${this::class.simpleName}"
}

sealed class UnaryOperation<In, out Out> : Operation() {
    abstract fun apply(input: In): Out

    object ToString : UnaryOperation<Any, String>(), StableHash by StableHash.of("UnaryOperation.ToString") {
        override fun apply(input: Any): String = input.toString()
    }

    object SizeOf : UnaryOperation<Collection<*>, Int>(), StableHash by StableHash.of("UnaryOperation.SizeOf") {
        override fun apply(input: Collection<*>): Int = input.size
    }

    object AbsoluteValue : UnaryOperation<Int, Int>(), StableHash by StableHash.of("UnaryOperation.AbsoluteValue") {
        override fun apply(input: Int): Int = input.absoluteValue
    }

    object AbsoluteValueKroner : UnaryOperation<Kroner, Kroner>(), StableHash by StableHash.of("UnaryOperation.AbsoluteValueKroner") {
        override fun apply(input: Kroner): Kroner = Kroner(input.value.absoluteValue)
    }

    class Select<In : Any, Out>(val selector: TemplateModelSelector<In, Out>) : UnaryOperation<In, Out>() {
        override fun apply(input: In): Out = selector.selector(input)
        override fun stableHashCode(): Int = StableHash.of(StableHash.of("UnaryOperation.Select"), StableHash.of(selector)).stableHashCode()
    }

    class SafeCall<In : Any, Out>(val selector: TemplateModelSelector<In, Out>) : UnaryOperation<In?, Out?>() {
        override fun apply(input: In?): Out? = input?.let { selector.selector(it) }
        override fun stableHashCode(): Int = StableHash.of(StableHash.of("UnaryOperation.SafeCall"), StableHash.of(selector)).stableHashCode()
    }

    object Not : UnaryOperation<Boolean, Boolean>(), StableHash by StableHash.of("UnaryOperation.Not") {
        override fun apply(input: Boolean): Boolean = input.not()
    }

    data class MapCollection<In, Out>(val mapper: UnaryOperation<In, Out>) : UnaryOperation<Collection<In>, Collection<Out>>() {
        override fun apply(input: Collection<In>): Collection<Out> = input.map { mapper.apply(it) }
        override fun stableHashCode(): Int = hashCode()
    }

    object IsEmpty : UnaryOperation<Collection<*>, Boolean>(), StableHash by StableHash.of("UnaryOperation.IsEmpty") {
        override fun apply(input: Collection<*>): Boolean = input.isEmpty()
    }

    object FunksjonsbryterEnabled : UnaryOperation<UnleashToggle, Boolean>(), StableHash by StableHash.of("UnaryOperation.Enabled") {
        override fun apply(input: UnleashToggle): Boolean = input.isEnabled()
    }

    object BrukerFulltNavn: UnaryOperation<Bruker, String>(), StableHash by StableHash.of("UnaryOperation.BrukerFulltNavn") {
        override fun apply(input: Bruker): String = input.fulltNavn()
    }
}

abstract class BinaryOperation<in In1, in In2, out Out>(val doc: Documentation? = null) : Operation() {
    data class Documentation(val name: String, val syntax: Notation) {
        enum class Notation { PREFIX, INFIX, POSTFIX, FUNCTION }
    }

    abstract fun apply(first: In1, second: In2): Out

    class Equal<In> : BinaryOperation<In, In, Boolean>(Documentation("==", Documentation.Notation.INFIX)),
        StableHash by StableHash.of("BinaryOperation.Equal") {
        override fun apply(first: In, second: In): Boolean = first == second
    }

    class GreaterThan<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>(Documentation(">", Documentation.Notation.INFIX)),
        StableHash by StableHash.of("BinaryOperation.GreaterThan") {
        override fun apply(first: T, second: T): Boolean = first > second
    }

    class GreaterThanOrEqual<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>(Documentation(">=", Documentation.Notation.INFIX)),
        StableHash by StableHash.of("BinaryOperation.GreaterThanOrEqual") {
        override fun apply(first: T, second: T): Boolean = first >= second
    }

    class LessThanOrEqual<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>(Documentation("<=", Documentation.Notation.INFIX)),
        StableHash by StableHash.of("BinaryOperation.LessThanOrEqual") {
        override fun apply(first: T, second: T): Boolean = first <= second
    }

    class LessThan<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>(Documentation("<", Documentation.Notation.INFIX)),
        StableHash by StableHash.of("BinaryOperation.LessThan") {
        override fun apply(first: T, second: T): Boolean = first < second
    }

    object Or : BinaryOperation<Boolean, Boolean, Boolean>(Documentation("or", Documentation.Notation.INFIX)),
        StableHash by StableHash.of("BinaryOperation.Or") {
        override fun apply(first: Boolean, second: Boolean): Boolean = first || second
    }

    object And : BinaryOperation<Boolean, Boolean, Boolean>(Documentation("and", Documentation.Notation.INFIX)),
        StableHash by StableHash.of("BinaryOperation.And") {
        override fun apply(first: Boolean, second: Boolean): Boolean = first && second
    }

    object Concat : BinaryOperation<String, String, String>(), StableHash by StableHash.of("BinaryOperation.Concat") {
        override fun apply(first: String, second: String): String = first + second
    }

    class IntPlus<T : IntValue>(val constructor: (Int) -> T) : BinaryOperation<T, T, T>(), StableHash by StableHash.of("BinaryOperation.IntPlus") {
        override fun apply(first: T, second: T): T = constructor(first.value + second.value)
    }

    class IfNull<T : Any> : BinaryOperation<T?, T, T>(Documentation("?:", Documentation.Notation.INFIX)),
        StableHash by StableHash.of("BinaryOperation.IfNull") {
        override fun apply(first: T?, second: T): T = first ?: second
    }

    data class MapCollection<In1, In2, Out>(val mapper: BinaryOperation<In1, In2, Out>) : BinaryOperation<Collection<In1>, In2, Collection<Out>>() {
        override fun apply(first: Collection<In1>, second: In2): Collection<Out> = first.map { mapper.apply(it, second) }
        override fun stableHashCode(): Int = hashCode()
    }

    class EnumInList<EnumType : Enum<*>> : BinaryOperation<EnumType, List<EnumType>, Boolean>(), StableHash by StableHash.of("BinaryOperation.EnumInList") {
        override fun apply(first: EnumType, second: List<EnumType>): Boolean = second.contains(first)
    }

    class GetElementOrNull<ListType> : BinaryOperation<List<ListType>?, Int, ListType?>(), StableHash by StableHash.of("BinaryOperation.GetElementOrNull") {
        override fun apply(first: List<ListType>?, second: Int): ListType? = first?.getOrNull(second)
    }

    class IfElse<Out> : BinaryOperation<Boolean, Pair<Out, Out>, Out>(), StableHash by StableHash.of("BinaryOperation.IfElse") {
        override fun apply(first: Boolean, second: Pair<Out, Out>): Out = if (first) second.first else second.second
    }

    class Tuple<Out> : BinaryOperation<Out, Out, Pair<Out, Out>>(), StableHash by StableHash.of("BinaryOperation.Tuple") {
        override fun apply(first: Out, second: Out): Pair<Out, Out> = first to second
    }

    data class Flip<In1, In2, Out>(val operation: BinaryOperation<In2, In1, Out>) : BinaryOperation<In1, In2, Out>() {
        override fun apply(first: In1, second: In2): Out = operation.apply(second, first)
        override fun stableHashCode(): Int = hashCode()
    }

    data class SafeCall<In1, In2, Out>(val operation: BinaryOperation<In1 & Any, In2 & Any, Out>) : BinaryOperation<In1, In2, Out?>() {
        override fun apply(first: In1, second: In2): Out? =
            if (first != null && second != null) {
                operation.apply(first, second)
            } else null

        override fun stableHashCode(): Int = StableHash.hash(StableHash.of("BinaryOperation.SafeCall" ), operation)
    }

}