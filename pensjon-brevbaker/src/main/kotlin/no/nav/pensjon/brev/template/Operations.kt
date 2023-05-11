package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.expression.Predicate
import no.nav.pensjon.brevbaker.api.model.*
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.FormatStyle
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

    class ToString<T : Any> : UnaryOperation<T, String>() {
        override fun apply(input: T): String = input.toString()
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
    object FormatFoedselsnummer : UnaryOperation<Foedselsnummer, String>() {
        override fun apply(input: Foedselsnummer): String = input.format()
    }

    data class Select<In: Any, Out>(val selector: TemplateModelSelector<In, Out>) : UnaryOperation<In, Out>() {
        override fun apply(input: In): Out = selector.selector(input)
    }

    data class SafeCall<In: Any, Out>(val selector: TemplateModelSelector<In, Out>) : UnaryOperation<In?, Out?>() {
        override fun apply(input: In?): Out? = input?.let { selector.selector(it) }
    }

    data class IfNull<In>(val then: In) : UnaryOperation<In?, In>() {
        override fun apply(input: In?): In = input ?: then
    }

    object Not : UnaryOperation<Boolean, Boolean>() {
        override fun apply(input: Boolean): Boolean = input.not()
    }

    data class MapCollection<In, Out>(val mapper: UnaryOperation<In, Out>): UnaryOperation<Collection<In>, Collection<Out>>() {
        override fun apply(input: Collection<In>): Collection<Out> = input.map { mapper.apply(it) }
    }
}

abstract class BinaryOperation<in In1, in In2, out Out> : Operation() {

    abstract fun apply(first: In1, second: In2): Out

    class Equal<In> : BinaryOperation<In, In, Boolean>() {
        override fun apply(first: In, second: In): Boolean = first == second
    }

    class GreaterThan<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>() {
        override fun apply(first: T, second: T): Boolean = first > second
    }

    class GreaterThanOrEqual<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>() {
        override fun apply(first: T, second: T): Boolean = first >= second
    }

    class LessThanOrEqual<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>() {
        override fun apply(first: T, second: T): Boolean = first <= second
    }

    class LessThan<in T : Comparable<T>> : BinaryOperation<T, T, Boolean>() {
        override fun apply(first: T, second: T): Boolean = first < second
    }

    object Or : BinaryOperation<Boolean, Boolean, Boolean>() {
        override fun apply(first: Boolean, second: Boolean): Boolean = first || second
    }

    object And : BinaryOperation<Boolean, Boolean, Boolean>() {
        override fun apply(first: Boolean, second: Boolean): Boolean = first && second
    }

    object Concat : BinaryOperation<String, String, String>() {
        override fun apply(first: String, second: String): String = first + second
    }

    object LocalizedShortDateFormat : BinaryOperation<LocalDate, Language, String>() {
        override fun apply(first: LocalDate, second: Language): String =
            first.format(dateFormatter(second, FormatStyle.SHORT)).replace(' ', ' ') //space to non braking space
    }

    object LocalizedDateFormat : BinaryOperation<LocalDate, Language, String>() {
        override fun apply(first: LocalDate, second: Language): String =
            first.format(dateFormatter(second, FormatStyle.LONG)).replace(' ', ' ') //space to non braking space
    }

    object LocalizedDoubleFormat : BinaryOperation<Double, Language, String>() {
        override fun apply(first: Double, second: Language): String =
            String.format(second.locale(), "%.2f", first)
    }

    object LocalizedIntFormat : BinaryOperation<Int, Language, String>() {
        override fun apply(first: Int, second: Language): String =
            String.format(second.locale(), "%d", first)
    }

    object LocalizedCurrencyFormat : BinaryOperation<Int, Language, String>() {
        override fun apply(first: Int, second: Language): String =
            NumberFormat.getNumberInstance(second.locale())
                .apply { maximumFractionDigits = 0 }
                .format(first)
    }

    object LocalizedCollectionFormat : BinaryOperation<Collection<String>, Language, String>() {
        override fun apply(first: Collection<String>, second: Language): String {
            return if (first.size == 1) {
                first.first()
            } else {
                val lastSeparator = when (second) {
                    Language.Bokmal -> " og "
                    Language.Nynorsk -> " og "
                    Language.English -> " and "
                }
                first.take(first.size - 1).joinToString(", ") + lastSeparator + first.last()
            }
        }

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

    class ValidatePredicate<T> : BinaryOperation<Predicate<T>, T, Boolean>() {
        override fun apply(first: Predicate<T>, second: T): Boolean = first.validate(second)
    }

    data class Flip<In1, In2, Out>(val operation: BinaryOperation<In2, In1, Out>): BinaryOperation<In1, In2, Out>() {
        override fun apply(first: In1, second: In2): Out = operation.apply(second, first)
    }

}