package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.render.fulltNavn
import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.template.expression.ExpressionMapper
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.util.Objects
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

    operator fun invoke(input: Expression<In>): Expression<Out> = Expression.UnaryInvoke(input, this)

    object AbsoluteValue : UnaryOperation<Int, Int>(), StableHash by StableHash.of("UnaryOperation.AbsoluteValue") {
        override fun apply(input: Int): Int = input.absoluteValue
    }

    object AbsoluteValueKroner : UnaryOperation<Kroner, Kroner>(), StableHash by StableHash.of("UnaryOperation.AbsoluteValueKroner") {
        override fun apply(input: Kroner): Kroner = Kroner(input.value.absoluteValue)
    }

    object BrukerFulltNavn : UnaryOperation<Bruker, String>(), StableHash by StableHash.of("UnaryOperation.BrukerFulltNavn") {
        override fun apply(input: Bruker): String = input.fulltNavn()
    }

    object FunksjonsbryterEnabled : UnaryOperation<FeatureToggle, Boolean>(), StableHash by StableHash.of("UnaryOperation.Enabled") {
        override fun apply(input: FeatureToggle): Boolean = FeatureToggleSingleton.isEnabled(input)
    }

    object IsEmpty : UnaryOperation<Collection<*>, Boolean>(), StableHash by StableHash.of("UnaryOperation.IsEmpty") {
        override fun apply(input: Collection<*>): Boolean = input.isEmpty()
    }

    class MapValue<In, Out> internal constructor(val mapper: ExpressionMapper<In, Out>) : UnaryOperation<In, Out>(), StableHash {
        override fun apply(input: In): Out = mapper.apply(input)
        override fun stableHashCode(): Int = StableHash.of(StableHash.of("UnaryOperation.MapValue"), mapper).stableHashCode()
        override fun equals(other: Any?): Boolean {
            if (other !is MapValue<*, *>) return false
            return mapper == other.mapper && super.equals(other)
        }
        override fun hashCode() = Objects.hash(mapper, super.hashCode())
        override fun toString() = "MapValue(mapper=$mapper)"
    }

    class MapCollection<In, Out> internal constructor(val mapper: UnaryOperation<In, Out>) : UnaryOperation<Collection<In>, Collection<Out>>() {
        override fun apply(input: Collection<In>): Collection<Out> = input.map { mapper.apply(it) }
        override fun stableHashCode(): Int = StableHash.of(StableHash.of("UnaryOperation.MapCollection"), mapper).stableHashCode()

        override fun equals(other: Any?): Boolean {
            if (other !is MapCollection<*,*>) return false
            return mapper == other.mapper && super.equals(other)
        }
        override fun hashCode() = Objects.hash(mapper, super.hashCode())
        override fun toString() = "MapCollection(mapper=$mapper)"
    }

    object Not : UnaryOperation<Boolean, Boolean>(), StableHash by StableHash.of("UnaryOperation.Not") {
        override fun apply(input: Boolean): Boolean = input.not()
    }

    class SafeCall<In : Any, Out>(val selector: TemplateModelSelector<In, Out>) : UnaryOperation<In?, Out?>() {
        override fun apply(input: In?): Out? = input?.let { selector.selector(it) }
        override fun stableHashCode(): Int = StableHash.of(StableHash.of("UnaryOperation.SafeCall"), selector).stableHashCode()
    }

    class Select<In : Any, Out>(val selector: TemplateModelSelector<In, Out>) : UnaryOperation<In, Out>() {
        override fun apply(input: In): Out = selector.selector(input)
        override fun stableHashCode(): Int = StableHash.of(StableHash.of("UnaryOperation.Select"), StableHash.of(selector)).stableHashCode()
    }

    object SizeOf : UnaryOperation<Collection<*>, Int>(), StableHash by StableHash.of("UnaryOperation.SizeOf") {
        override fun apply(input: Collection<*>): Int = input.size
    }

    object ToString : UnaryOperation<Any, String>(), StableHash by StableHash.of("UnaryOperation.ToString") {
        override fun apply(input: Any): String = input.toString()
    }

    object QuotationStart : UnaryOperation<Language, String>(), StableHash by StableHash.of("QuotationStart") {
        override fun apply(input: Language): String =
            when (input) {
                Language.Bokmal -> "«"
                Language.Nynorsk -> "«"
                Language.English -> "'"
            }
    }

    object QuotationEnd : UnaryOperation<Language, String>(), StableHash by StableHash.of("QuotationEnd") {
        override fun apply(input: Language): String =
            when (input) {
                Language.Bokmal -> "»"
                Language.Nynorsk -> "»"
                Language.English -> "'"
            }
    }
}

abstract class BinaryOperation<in In1, in In2, out Out>(val doc: Documentation? = null) : Operation() {
    class Documentation(val name: String, val syntax: Notation) {
        enum class Notation { PREFIX, INFIX, POSTFIX, FUNCTION }

        override fun equals(other: Any?): Boolean {
            if (other !is Documentation) return false
            return name == other.name && syntax == other.syntax && super.equals(other)
        }
        override fun hashCode() = Objects.hash(name, syntax, super.hashCode())
        override fun toString() = "Documentation(name='$name', syntax=$syntax)"
    }

    abstract fun apply(first: In1, second: In2): Out

    operator fun invoke(first: Expression<In1>, second: Expression<In2>): Expression<Out> =
        Expression.BinaryInvoke(first, second, this)

    class Equal<In> internal constructor(): BinaryOperation<In, In, Boolean>(Documentation("==", Documentation.Notation.INFIX)),
        StableHash by StableHash.of("BinaryOperation.Equal") {
        override fun apply(first: In, second: In): Boolean = first == second
    }

    class GreaterThan<in T : Comparable<T>> internal constructor(): BinaryOperation<T, T, Boolean>(Documentation(">", Documentation.Notation.INFIX)),
        StableHash by StableHash.of("BinaryOperation.GreaterThan") {
        override fun apply(first: T, second: T): Boolean = first > second
    }

    class GreaterThanOrEqual<in T : Comparable<T>> internal constructor(): BinaryOperation<T, T, Boolean>(Documentation(">=", Documentation.Notation.INFIX)),
        StableHash by StableHash.of("BinaryOperation.GreaterThanOrEqual") {
        override fun apply(first: T, second: T): Boolean = first >= second
    }

    class LessThanOrEqual<in T : Comparable<T>> internal constructor(): BinaryOperation<T, T, Boolean>(Documentation("<=", Documentation.Notation.INFIX)),
        StableHash by StableHash.of("BinaryOperation.LessThanOrEqual") {
        override fun apply(first: T, second: T): Boolean = first <= second
    }

    class LessThan<in T : Comparable<T>> internal constructor(): BinaryOperation<T, T, Boolean>(Documentation("<", Documentation.Notation.INFIX)),
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

    object IntMinus : BinaryOperation<Int, Int, Int>(Documentation("-", Documentation.Notation.INFIX)), StableHash by StableHash.of("BinaryOperation.IntMinus") {
        override fun apply(first: Int, second: Int): Int = first - second
    }

    object IntPlus : BinaryOperation<Int, Int, Int>(Documentation("+", Documentation.Notation.INFIX)), StableHash by StableHash.of("BinaryOperation.IntPlus") {
        override fun apply(first: Int, second: Int): Int = first + second
    }

    class IfNull<T : Any> internal constructor(): BinaryOperation<T?, T, T>(Documentation("?:", Documentation.Notation.INFIX)),
        StableHash by StableHash.of("BinaryOperation.IfNull") {
        override fun apply(first: T?, second: T): T = first ?: second
    }

    class MapCollection<In1, In2, Out> internal constructor(val mapper: BinaryOperation<In1, In2, Out>) : BinaryOperation<Collection<In1>, In2, Collection<Out>>() {
        override fun apply(first: Collection<In1>, second: In2): Collection<Out> = first.map { mapper.apply(it, second) }
        override fun stableHashCode(): Int = hashCode()

        override fun equals(other: Any?): Boolean {
            if (other !is MapCollection<*, *, *>) return false
            return mapper == other.mapper && doc == other.doc
        }
        override fun hashCode() = Objects.hash(mapper, doc)
        override fun toString() = "MapCollection(mapper=$mapper,doc=$doc)"
    }

    class EnumInList<EnumType : Enum<*>> internal constructor(): BinaryOperation<EnumType, List<EnumType>, Boolean>(), StableHash by StableHash.of("BinaryOperation.EnumInList") {
        override fun apply(first: EnumType, second: List<EnumType>): Boolean = second.contains(first)
    }

    class GetElementOrNull<ListType> internal constructor(): BinaryOperation<List<ListType>?, Int, ListType?>(), StableHash by StableHash.of("BinaryOperation.GetElementOrNull") {
        override fun apply(first: List<ListType>?, second: Int): ListType? = first?.getOrNull(second)
    }

    class IfElse<Out> internal constructor(): BinaryOperation<Boolean, Pair<Out, Out>, Out>(), StableHash by StableHash.of("BinaryOperation.IfElse") {
        override fun apply(first: Boolean, second: Pair<Out, Out>): Out = if (first) second.first else second.second
    }

    class Tuple<In1, In2> internal constructor(): BinaryOperation<In1, In2, Pair<In1, In2>>(), StableHash by StableHash.of("BinaryOperation.Tuple") {
        override fun apply(first: In1, second: In2): Pair<In1, In2> = first to second
    }

    class Flip<In1, In2, Out> internal constructor(val operation: BinaryOperation<In2, In1, Out>) : BinaryOperation<In1, In2, Out>() {
        override fun apply(first: In1, second: In2): Out = operation.apply(second, first)
        override fun stableHashCode(): Int = hashCode()

        override fun equals(other: Any?): Boolean {
            if (other !is Flip<*, *, *>) return false
            return operation == other.operation && doc == other.doc
        }
        override fun hashCode() = Objects.hash(operation, doc)
        override fun toString() = "Flip(operation=$operation,doc=$doc)"
    }

    class SafeCall<In1, In2, Out> internal constructor(val operation: BinaryOperation<In1 & Any, In2 & Any, Out>) : BinaryOperation<In1, In2, Out?>() {
        override fun apply(first: In1, second: In2): Out? =
            if (first != null && second != null) {
                operation.apply(first, second)
            } else null

        override fun stableHashCode(): Int = StableHash.hash(StableHash.of("BinaryOperation.SafeCall"), operation)

        override fun equals(other: Any?): Boolean {
            if (other !is SafeCall<*, *, *>) return false
            return operation == other.operation && doc == other.doc
        }
        override fun hashCode() = Objects.hash(operation, doc)
        override fun toString(): String = "SafeCall(operation=$operation,doc=$doc)"

    }

}