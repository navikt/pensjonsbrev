package no.nav.pensjon.brev.template

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.template.BinaryOperation.Documentation
import no.nav.pensjon.brev.template.expression.ExpressionMapper
import no.nav.pensjon.brev.template.render.fulltNavn
import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.Kroner
import kotlin.math.absoluteValue

abstract class AbstractOperation : Operation {
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

@InterneDataklasser
sealed class UnaryOperationImpl<In, out Out> : UnaryOperation<In, Out>, AbstractOperation() {

    @InterneDataklasser
    object AbsoluteValueImpl : UnaryOperation.AbsoluteValue, AbstractOperation(), StableHash by StableHash.of("UnaryOperation.AbsoluteValue") {
        override fun apply(input: Int): Int = input.absoluteValue
    }

    @InterneDataklasser
    object AbsoluteValueKronerImpl : UnaryOperation.AbsoluteValueKroner, AbstractOperation(), StableHash by StableHash.of("UnaryOperation.AbsoluteValueKroner") {
        override fun apply(input: Kroner): Kroner = Kroner(input.value.absoluteValue)
    }

    @InterneDataklasser
    object BrukerFulltNavnImpl : UnaryOperation.BrukerFulltNavn, AbstractOperation(), StableHash by StableHash.of("UnaryOperation.BrukerFulltNavn") {
        override fun apply(input: Bruker): String = input.fulltNavn()
    }

    @InterneDataklasser
    object FunksjonsbryterEnabledImpl : UnaryOperation.FunksjonsbryterEnabled, AbstractOperation(), StableHash by StableHash.of("UnaryOperation.Enabled") {
        override fun apply(input: FeatureToggle): Boolean = FeatureToggleSingleton.isEnabled(input)
    }

    @InterneDataklasser
    object IsEmptyImpl : UnaryOperation.IsEmpty, AbstractOperation(), StableHash by StableHash.of("UnaryOperation.IsEmpty") {
        override fun apply(input: Collection<*>): Boolean = input.isEmpty()
    }

    @InterneDataklasser
    data class MapValueImpl<In, Out>(override val mapper: ExpressionMapper<In, Out>) : UnaryOperation.MapValue<In, Out>, AbstractOperation(), StableHash {
        override fun apply(input: In): Out = mapper.apply(input)
        override fun stableHashCode(): Int = StableHash.of(StableHash.of("UnaryOperation.MapValue"), mapper).stableHashCode()
    }

    @InterneDataklasser
    data class MapCollectionImpl<In, Out>(override val mapper: UnaryOperation<In, Out>) : UnaryOperation.MapCollection<In, Out>, AbstractOperation() {
        override fun apply(input: Collection<In>): Collection<Out> = input.map { mapper.apply(it) }
        override fun stableHashCode(): Int = StableHash.of(StableHash.of("UnaryOperation.MapCollection"), mapper).stableHashCode()
    }

    @InterneDataklasser
    object Not : UnaryOperation.Not, AbstractOperation(), StableHash by StableHash.of("UnaryOperation.Not") {
        override fun apply(input: Boolean): Boolean = input.not()
    }

    @InterneDataklasser
    class SafeCall<In : Any, Out>(override val selector: TemplateModelSelector<In, Out>) : UnaryOperation.SafeCall<In, Out>, AbstractOperation() {
        override fun apply(input: In?): Out? = input?.let { selector.selector(it) }
        override fun stableHashCode(): Int = StableHash.of(StableHash.of("UnaryOperation.SafeCall"), selector).stableHashCode()
    }

    @InterneDataklasser
    class Select<In : Any, Out>(override val selector: TemplateModelSelector<In, Out>) : UnaryOperation.Select<In, Out>, AbstractOperation() {
        override fun apply(input: In): Out = selector.selector(input)
        override fun stableHashCode(): Int = StableHash.of(StableHash.of("UnaryOperation.Select"), StableHash.of(selector)).stableHashCode()
    }

    @InterneDataklasser
    object SizeOf : UnaryOperation.SizeOf, AbstractOperation(), StableHash by StableHash.of("UnaryOperation.SizeOf") {
        override fun apply(input: Collection<*>): Int = input.size
    }

    @InterneDataklasser
    object ToString : UnaryOperation.ToString, AbstractOperation(), StableHash by StableHash.of("UnaryOperation.ToString") {
        override fun apply(input: Any): String = input.toString()
    }
}

@InterneDataklasser
abstract class BinaryOperationImpl<in In1, in In2, out Out>(val doc: Documentation? = null) : AbstractOperation() {

    @InterneDataklasser
    data class DocumentationImpl(override val name: String, override val syntax: Documentation.Notation) : Documentation

    @InterneDataklasser
    class EqualImpl<In> : BinaryOperation.Equal<In>,
        AbstractOperation(), StableHash by StableHash.of("BinaryOperation.Equal") {
        override val doc = DocumentationImpl("==", Documentation.Notation.INFIX)
        override fun apply(first: In, second: In): Boolean = first == second
    }

    @InterneDataklasser
    class GreaterThan<in T : Comparable<T>> : BinaryOperation.GreaterThan<T>,
        AbstractOperation(), StableHash by StableHash.of("BinaryOperation.GreaterThan") {
        override val doc = DocumentationImpl(">", Documentation.Notation.INFIX)
        override fun apply(first: T, second: T): Boolean = first > second
    }

    @InterneDataklasser
    class GreaterThanOrEqual<in T : Comparable<T>> : BinaryOperation.GreaterThanOrEqual<T>,
        AbstractOperation(), StableHash by StableHash.of("BinaryOperation.GreaterThanOrEqual") {
        override val doc = DocumentationImpl(">=", Documentation.Notation.INFIX)
        override fun apply(first: T, second: T): Boolean = first >= second
    }

    @InterneDataklasser
    class LessThanOrEqual<in T : Comparable<T>> : BinaryOperation.LessThanOrEqual<T>,
        AbstractOperation(), StableHash by StableHash.of("BinaryOperation.LessThanOrEqual") {
        override val doc = DocumentationImpl("<=", Documentation.Notation.INFIX)
        override fun apply(first: T, second: T): Boolean = first <= second
    }

    @InterneDataklasser
    class LessThan<in T : Comparable<T>> : BinaryOperation.LessThan<T>,
        AbstractOperation(), StableHash by StableHash.of("BinaryOperation.LessThan") {
        override val doc = DocumentationImpl("<", Documentation.Notation.INFIX)
        override fun apply(first: T, second: T): Boolean = first < second
    }

    @InterneDataklasser
    object Or : BinaryOperation.Or,
        AbstractOperation(), StableHash by StableHash.of("BinaryOperation.Or") {
        override val doc = DocumentationImpl("or", Documentation.Notation.INFIX)
        override fun apply(first: Boolean, second: Boolean): Boolean = first || second
    }

    @InterneDataklasser
    object And : BinaryOperation.And,
        AbstractOperation(), StableHash by StableHash.of("BinaryOperation.And") {
        override val doc = DocumentationImpl("and", Documentation.Notation.INFIX)
        override fun apply(first: Boolean, second: Boolean): Boolean = first && second
    }

    @InterneDataklasser
    object Concat : BinaryOperation.Concat, AbstractOperation(), StableHash by StableHash.of("BinaryOperation.Concat") {
        override fun apply(first: String, second: String): String = first + second
    }

    @InterneDataklasser
    object IntMinus : BinaryOperation.IntMinus, AbstractOperation(), StableHash by StableHash.of("BinaryOperation.IntMinus") {
        override val doc = DocumentationImpl("-", Documentation.Notation.INFIX)
        override fun apply(first: Int, second: Int): Int = first - second
    }

    @InterneDataklasser
    object IntPlus : BinaryOperation.IntPlus, AbstractOperation(), StableHash by StableHash.of("BinaryOperation.IntPlus") {
        override val doc = DocumentationImpl("+", Documentation.Notation.INFIX)
        override fun apply(first: Int, second: Int): Int = first + second
    }

    @InterneDataklasser
    class IfNull<T : Any> : BinaryOperation.IfNull<T>, AbstractOperation(),
        StableHash by StableHash.of("BinaryOperation.IfNull") {
        override val doc = DocumentationImpl("?:", Documentation.Notation.INFIX)
        override fun apply(first: T?, second: T): T = first ?: second
    }

    @InterneDataklasser
    data class MapCollection<In1, In2, Out>(override val mapper: BinaryOperation<In1, In2, Out>) : BinaryOperation.MapCollection<In1, In2, Out>, AbstractOperation() {
        override fun apply(first: Collection<In1>, second: In2): Collection<Out> = first.map { mapper.apply(it, second) }
        override fun stableHashCode(): Int = hashCode()
    }

    @InterneDataklasser
    class EnumInListImpl<EnumType : Enum<*>> : BinaryOperation.EnumInList<EnumType>, AbstractOperation(), StableHash by StableHash.of("BinaryOperation.EnumInList") {
        override fun apply(first: EnumType, second: List<EnumType>): Boolean = second.contains(first)
    }

    @InterneDataklasser
    class GetElementOrNullImpl<ListType> : BinaryOperation.GetElementOrNull<ListType>, AbstractOperation(), StableHash by StableHash.of("BinaryOperation.GetElementOrNull") {
        override fun apply(first: List<ListType>?, second: Int): ListType? = first?.getOrNull(second)
    }

    @InterneDataklasser
    class IfElse<Out> : BinaryOperation.IfElse<Out>, AbstractOperation(), StableHash by StableHash.of("BinaryOperation.IfElse") {
        override fun apply(first: Boolean, second: Pair<Out, Out>): Out = if (first) second.first else second.second
    }

    @InterneDataklasser
    class Tuple<Out> : BinaryOperation.Tuple<Out>, AbstractOperation(), StableHash by StableHash.of("BinaryOperation.Tuple") {
        override fun apply(first: Out, second: Out): Pair<Out, Out> = first to second
    }

    @InterneDataklasser
    data class Flip<In1, In2, Out>(override val operation: BinaryOperation<In2, In1, Out>) : BinaryOperation.Flip<In1, In2, Out>, AbstractOperation() {
        override fun apply(first: In1, second: In2): Out = operation.apply(second, first)
        override fun stableHashCode(): Int = hashCode()
    }

    @InterneDataklasser
    data class SafeCall<In1, In2, Out>(override val operation: BinaryOperation<In1 & Any, In2 & Any, Out>) : BinaryOperation.SafeCall<In1, In2, Out?>, AbstractOperation() {
        override fun apply(first: In1, second: In2): Out? =
            if (first != null && second != null) {
                operation.apply(first, second)
            } else null

        override fun stableHashCode(): Int = StableHash.hash(StableHash.of("BinaryOperation.SafeCall"), operation)
    }
}