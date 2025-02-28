package no.nav.pensjon.brev.template

import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.template.expression.ExpressionMapper
import no.nav.pensjon.brevbaker.api.model.Kroner

interface Operation : StableHash

sealed interface UnaryOperation<In, out Out> : Operation {
    fun apply(input: In): Out

    interface AbsoluteValue : UnaryOperation<Int, Int>

    interface AbsoluteValueKroner : UnaryOperation<Kroner, Kroner>

    interface BrukerFulltNavn : UnaryOperation<Bruker, String>

    interface FunksjonsbryterEnabled : UnaryOperation<FeatureToggle, Boolean>

    interface IsEmpty : UnaryOperation<Collection<*>, Boolean>

    interface MapValue<In, Out> : UnaryOperation<In, Out> {
        val mapper: ExpressionMapper<In, Out>
    }

    interface MapCollection<In, Out> : UnaryOperation<Collection<In>, Collection<Out>> {
        val mapper: UnaryOperation<In, Out>
    }

    interface Not : UnaryOperation<Boolean, Boolean>

    interface SafeCall<In : Any, Out> : UnaryOperation<In?, Out?> {
        val selector: TemplateModelSelector<In, Out>
    }

    interface Select<In : Any, Out> : UnaryOperation<In, Out> {
        val selector: TemplateModelSelector<In, Out>
    }

    interface SizeOf : UnaryOperation<Collection<*>, Int>

    interface ToString : UnaryOperation<Any, String>
}

interface BinaryOperation<in In1, in In2, out Out> : Operation {
    val doc: Documentation?
        get() = null

    data class Documentation(val name: String, val syntax: Notation) {
        enum class Notation { PREFIX, INFIX, POSTFIX, FUNCTION }
    }

    fun apply(first: In1, second: In2): Out

    class Equal<In> : BinaryOperation<In, In, Boolean>,
        AbstractOperation(), StableHash by StableHash.of("BinaryOperation.Equal") {
        override val doc = Documentation("==", Documentation.Notation.INFIX)
        override fun apply(first: In, second: In): Boolean = first == second
    }

    class GreaterThan<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>,
        AbstractOperation(), StableHash by StableHash.of("BinaryOperation.GreaterThan") {
        override val doc = Documentation(">", Documentation.Notation.INFIX)
        override fun apply(first: T, second: T): Boolean = first > second
    }

    class GreaterThanOrEqual<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>,
        AbstractOperation(), StableHash by StableHash.of("BinaryOperation.GreaterThanOrEqual") {
        override val doc = Documentation(">=", Documentation.Notation.INFIX)
        override fun apply(first: T, second: T): Boolean = first >= second
    }

    class LessThanOrEqual<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>,
        AbstractOperation(), StableHash by StableHash.of("BinaryOperation.LessThanOrEqual") {
        override val doc = Documentation("<=", Documentation.Notation.INFIX)
        override fun apply(first: T, second: T): Boolean = first <= second
    }

    class LessThan<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>,
        AbstractOperation(), StableHash by StableHash.of("BinaryOperation.LessThan") {
        override val doc = Documentation("<", Documentation.Notation.INFIX)
        override fun apply(first: T, second: T): Boolean = first < second
    }

    object Or : BinaryOperation<Boolean, Boolean, Boolean>,
        AbstractOperation(), StableHash by StableHash.of("BinaryOperation.Or") {
        override val doc = Documentation("or", Documentation.Notation.INFIX)
        override fun apply(first: Boolean, second: Boolean): Boolean = first || second
    }

    object And : BinaryOperation<Boolean, Boolean, Boolean>,
        AbstractOperation(), StableHash by StableHash.of("BinaryOperation.And") {
        override val doc = Documentation("and", Documentation.Notation.INFIX)
        override fun apply(first: Boolean, second: Boolean): Boolean = first && second
    }

    object Concat : BinaryOperation<String, String, String>, AbstractOperation(), StableHash by StableHash.of("BinaryOperation.Concat") {
        override fun apply(first: String, second: String): String = first + second
    }

    object IntMinus : BinaryOperation<Int, Int, Int>, AbstractOperation(), StableHash by StableHash.of("BinaryOperation.IntMinus") {
        override val doc = Documentation("-", Documentation.Notation.INFIX)
        override fun apply(first: Int, second: Int): Int = first - second
    }

    object IntPlus : BinaryOperation<Int, Int, Int>, AbstractOperation(), StableHash by StableHash.of("BinaryOperation.IntPlus") {
        override val doc = Documentation("+", Documentation.Notation.INFIX)
        override fun apply(first: Int, second: Int): Int = first + second
    }

    class IfNull<T : Any> : BinaryOperation<T?, T, T>, AbstractOperation(),
        StableHash by StableHash.of("BinaryOperation.IfNull") {
        override val doc = Documentation("?:", Documentation.Notation.INFIX)
        override fun apply(first: T?, second: T): T = first ?: second
    }

    data class MapCollection<In1, In2, Out>(val mapper: BinaryOperation<In1, In2, Out>) : BinaryOperation<Collection<In1>, In2, Collection<Out>>, AbstractOperation() {
        override fun apply(first: Collection<In1>, second: In2): Collection<Out> = first.map { mapper.apply(it, second) }
        override fun stableHashCode(): Int = hashCode()
    }

    class EnumInList<EnumType : Enum<*>> : BinaryOperation<EnumType, List<EnumType>, Boolean>, AbstractOperation(), StableHash by StableHash.of("BinaryOperation.EnumInList") {
        override fun apply(first: EnumType, second: List<EnumType>): Boolean = second.contains(first)
    }

    class GetElementOrNull<ListType> : BinaryOperation<List<ListType>?, Int, ListType?>, AbstractOperation(), StableHash by StableHash.of("BinaryOperation.GetElementOrNull") {
        override fun apply(first: List<ListType>?, second: Int): ListType? = first?.getOrNull(second)
    }

    class IfElse<Out> : BinaryOperation<Boolean, Pair<Out, Out>, Out>, AbstractOperation(), StableHash by StableHash.of("BinaryOperation.IfElse") {
        override fun apply(first: Boolean, second: Pair<Out, Out>): Out = if (first) second.first else second.second
    }

    class Tuple<Out> : BinaryOperation<Out, Out, Pair<Out, Out>>, AbstractOperation(), StableHash by StableHash.of("BinaryOperation.Tuple") {
        override fun apply(first: Out, second: Out): Pair<Out, Out> = first to second
    }

    data class Flip<In1, In2, Out>(val operation: BinaryOperation<In2, In1, Out>) : BinaryOperation<In1, In2, Out>, AbstractOperation() {
        override fun apply(first: In1, second: In2): Out = operation.apply(second, first)
        override fun stableHashCode(): Int = hashCode()
    }

    data class SafeCall<In1, In2, Out>(val operation: BinaryOperation<In1 & Any, In2 & Any, Out>) : BinaryOperation<In1, In2, Out?>, AbstractOperation() {
        override fun apply(first: In1, second: In2): Out? =
            if (first != null && second != null) {
                operation.apply(first, second)
            } else null

        override fun stableHashCode(): Int = StableHash.hash(StableHash.of("BinaryOperation.SafeCall"), operation)
    }

}