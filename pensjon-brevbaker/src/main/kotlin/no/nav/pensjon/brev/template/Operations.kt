package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.Telefonnummer
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.expression.Predicate
import java.time.LocalDate
import java.time.format.FormatStyle

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

    object FormatPhoneNumber : UnaryOperation<Telefonnummer, String>() {
        override fun apply(input: Telefonnummer): String = input.format()
    }

    data class Select<In, Out>(val select: In.() -> Out) : UnaryOperation<In, Out>() {
        override fun apply(input: In): Out = input.select()
    }

    data class IfNull<In>(val then: In) : UnaryOperation<In?, In>() {
        override fun apply(input: In?): In = input ?: then
    }

    object Not : UnaryOperation<Boolean, Boolean>() {
        override fun apply(input: Boolean): Boolean = input.not()
    }
}

sealed class BinaryOperation<in In1, in In2, out Out> : Operation() {

    abstract fun apply(first: In1, second: In2): Out

    class Equal<In> : BinaryOperation<In, In, Boolean>() {
        override fun apply(first: In, second: In): Boolean = first == second
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
            first.format(dateFormatter(second, FormatStyle.SHORT))
    }

    object LocalizedDateFormat : BinaryOperation<LocalDate, Language, String>() {
        override fun apply(first: LocalDate, second: Language): String =
            first.format(dateFormatter(second, FormatStyle.LONG))
    }

    object LocalizedDoubleFormat : BinaryOperation<Double, Language, String>() {
        override fun apply(first: Double, second: Language): String =
            String.format(second.locale(), "%.2f", first)
    }

    object LocalizedIntFormat : BinaryOperation<Int, Language, String>() {
        override fun apply(first: Int, second: Language): String =
                String.format(second.locale(), "%d", first)
    }
    class NullSafe<In1 : Any, In2 : Any, Out : Any>(private val operation: BinaryOperation<In1, In2, Out>) :
        BinaryOperation<In1?, In2?, Out?>() {
        override fun apply(first: In1?, second: In2?): Out? =
            if (first != null && second != null) {
                operation.apply(first, second)
            } else {
                null
            }
    }
    class EnumInList<EnumType : Enum<*>> : BinaryOperation<EnumType, List<EnumType>, Boolean>() {
        override fun apply(first: EnumType, second: List<EnumType>): Boolean = second.contains(first)
    }

    class EnumNotInList<EnumType : Enum<*>> : BinaryOperation<EnumType, List<EnumType>, Boolean>() {
        override fun apply(first: EnumType, second: List<EnumType>): Boolean = !second.contains(first)
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
}