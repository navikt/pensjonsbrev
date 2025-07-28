package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.expression.IntToKroner
import no.nav.pensjon.brev.template.expression.IntToYear
import no.nav.pensjon.brevbaker.api.model.IntValue
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year

val intValueSelector = object : TemplateModelSelector<IntValue, Int> {
    override val className: String = "no.nav.pensjon.brev.api.model.IntValue"
    override val propertyName: String = "value"
    override val propertyType: String = "kotlin.Int"
    override val selector = IntValue::value
}

private val Expression<IntValue>.value: Expression<Int>
    get() = UnaryOperation.Select(intValueSelector).invoke(this)

fun Expression<Int>.toKroner(): Expression<Kroner> =
    UnaryOperation.MapValue(IntToKroner).invoke(this)

fun Expression<Int>.toYear(): Expression<Year> =
    UnaryOperation.MapValue(IntToYear).invoke(this)

fun Expression<Double>.format(scale: Int = 2): Expression<String> =
    format(formatter = LocalizedFormatter.DoubleFormat(scale))

@JvmName("formatDoubleNullable")
fun Expression<Double?>.format(scale: Int = 2): Expression<String?> =
    format(formatter = LocalizedFormatter.DoubleFormat(scale))

@JvmName("formatInt")
fun Expression<Int>.format(): Expression<String> = format(formatter = LocalizedFormatter.IntFormat)

@JvmName("formatIntOrNull")
fun Expression<Int?>.format(): Expression<String?> = format(formatter = LocalizedFormatter.IntFormat)

operator fun Expression<Int>.plus(other: Int) = plus(other.expr())

operator fun Expression<Int>.plus(other: Expression<Int>): Expression<Int> =
    BinaryOperation.IntPlus(this, other)

@JvmName("kronerPlusInt")
operator fun Expression<Kroner>.plus(other: Int) = plus(other.expr().toKroner())

@JvmName("kronerPlus")
operator fun Expression<Kroner>.plus(other: Expression<Kroner>): Expression<Kroner> =
    (this.value + other.value).toKroner()

@JvmName("yearPlus")
operator fun Expression<Year>.plus(other: Int): Expression<Year> = (this.value + other).toYear()

operator fun Expression<Int>.minus(other: Int): Expression<Int> = minus(other.expr())

operator fun Expression<Int>.minus(other: Expression<Int>): Expression<Int> =
    BinaryOperation.IntMinus(this, other)

@JvmName("kronerMinusInt")
operator fun Expression<Kroner>.minus(other: Int): Expression<Kroner> = minus(other.expr().toKroner())

@JvmName("kronerMinus")
operator fun Expression<Kroner>.minus(other: Expression<Kroner>): Expression<Kroner> =
    (this.value - other.value).toKroner()

@JvmName("yearMinus")
operator fun Expression<Year>.minus(other: Int): Expression<Year> =
    (this.value - other).toYear()

infix fun <T : Comparable<T>> Expression<T>.greaterThan(compareTo: Expression<T>): Expression<Boolean> =
    BinaryOperation.GreaterThan<T>().invoke(this, compareTo)

infix fun <T : Comparable<T>> Expression<T>.greaterThanOrEqual(compareTo: Expression<T>): Expression<Boolean> =
    BinaryOperation.GreaterThanOrEqual<T>().invoke(this, compareTo)

infix fun <T : Comparable<T>> Expression<T>.lessThanOrEqual(compareTo: Expression<T>): Expression<Boolean> =
    BinaryOperation.LessThanOrEqual<T>().invoke(this, compareTo)

infix fun <T : Comparable<T>> Expression<T>.lessThan(compareTo: Expression<T>): Expression<Boolean> =
    BinaryOperation.LessThan<T>().invoke(this, compareTo)

// Literal compareTo value
infix fun <T : Comparable<T>> Expression<T>.greaterThan(compareTo: T): Expression<Boolean> =
    greaterThan(compareTo.expr())

infix fun <T : Comparable<T>> Expression<T>.greaterThanOrEqual(compareTo: T): Expression<Boolean> =
    greaterThanOrEqual(compareTo.expr())

infix fun <T : Comparable<T>> Expression<T>.lessThanOrEqual(compareTo: T): Expression<Boolean> =
    lessThanOrEqual(compareTo.expr())

infix fun <T : Comparable<T>> Expression<T>.lessThan(compareTo: T): Expression<Boolean> =
    lessThan(compareTo.expr())

// IntValue compareTo literal
infix fun Expression<IntValue>.greaterThan(compareTo: Int): Expression<Boolean> =
    value.greaterThan(compareTo)

infix fun Expression<IntValue>.greaterThanOrEqual(compareTo: Int): Expression<Boolean> =
    value.greaterThanOrEqual(compareTo)

infix fun Expression<IntValue>.lessThan(compareTo: Int): Expression<Boolean> = value.lessThan(compareTo)
infix fun Expression<IntValue>.lessThanOrEqual(compareTo: Int): Expression<Boolean> = value.lessThanOrEqual(compareTo)

// IntValue compareTo expression
@JvmName("greaterThanIntValue")
infix fun Expression<IntValue>.greaterThan(compareTo: Expression<IntValue>): Expression<Boolean> =
    value.greaterThan(compareTo.value)

@JvmName("greaterThanOrEqualIntValue")
infix fun Expression<IntValue>.greaterThanOrEqual(compareTo: Expression<IntValue>): Expression<Boolean> =
    value.greaterThanOrEqual(compareTo.value)

@JvmName("lessThanIntValue")
infix fun Expression<IntValue>.lessThan(compareTo: Expression<IntValue>): Expression<Boolean> =
    value.lessThan(compareTo.value)

@JvmName("lessThanOrEqualIntValue")
infix fun Expression<IntValue>.lessThanOrEqual(compareTo: Expression<IntValue>): Expression<Boolean> =
    value.lessThanOrEqual(compareTo.value)

// IntValue equals literal
infix fun Expression<IntValue>.equalTo(compareTo: Int): Expression<Boolean> = value.equalTo(compareTo)
infix fun Expression<IntValue>.notEqualTo(compareTo: Int): Expression<Boolean> = value.notEqualTo(compareTo)