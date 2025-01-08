package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brevbaker.api.model.IntValue
import no.nav.pensjon.brevbaker.api.model.Kroner

val intValueSelector = object : TemplateModelSelector<IntValue, Int> {
    override val className: String = "no.nav.pensjon.brev.api.model.IntValue"
    override val propertyName: String = "value"
    override val propertyType: String = "kotlin.Int"
    override val selector = IntValue::value
}

private val Expression<IntValue>.value: Expression<Int>
    get() = Expression.UnaryInvoke(
        this,
        UnaryOperation.Select(intValueSelector)
    )

fun Expression<Double>.format(): Expression<String> = format(formatter = LocalizedFormatter.DoubleFormat)

@JvmName("formatInt")
fun Expression<Int>.format(): Expression<String> = format(formatter = LocalizedFormatter.IntFormat)


operator fun Expression<Kroner>.plus(other: Expression<Kroner>): Expression<Kroner> =
    Expression.BinaryInvoke(
        this,
        other,
        BinaryOperation.IntPlus(::Kroner),
    )

infix fun <T: Comparable<T>> Expression<T>.greaterThan(compareTo: Expression<T>): Expression<Boolean> =
    Expression.BinaryInvoke(
        first = this,
        second = compareTo,
        operation = BinaryOperation.GreaterThan(),
    )

infix fun <T: Comparable<T>> Expression<T>.greaterThanOrEqual(compareTo: Expression<T>): Expression<Boolean> =
    Expression.BinaryInvoke(
        first = this,
        second = compareTo,
        operation = BinaryOperation.GreaterThanOrEqual(),
    )

infix fun <T: Comparable<T>> Expression<T>.lessThanOrEqual(compareTo: Expression<T>): Expression<Boolean> =
    Expression.BinaryInvoke(
        first = this,
        second = compareTo,
        operation = BinaryOperation.LessThanOrEqual(),
    )

infix fun <T: Comparable<T>> Expression<T>.lessThan(compareTo: Expression<T>): Expression<Boolean> =
    Expression.BinaryInvoke(
        first = this,
        second = compareTo,
        operation = BinaryOperation.LessThan(),
    )

// Literal compareTo value
infix fun <T: Comparable<T>> Expression<T>.greaterThan(compareTo: T): Expression<Boolean> = greaterThan(compareTo.expr())
infix fun <T: Comparable<T>> Expression<T>.greaterThanOrEqual(compareTo: T): Expression<Boolean> = greaterThanOrEqual(compareTo.expr())
infix fun <T: Comparable<T>> Expression<T>.lessThanOrEqual(compareTo: T): Expression<Boolean> = lessThanOrEqual(compareTo.expr())
infix fun <T: Comparable<T>> Expression<T>.lessThan(compareTo: T): Expression<Boolean> = lessThan(compareTo.expr())
// IntValue compareTo literal
infix fun Expression<IntValue>.greaterThan(compareTo: Int): Expression<Boolean> = value.greaterThan(compareTo)
infix fun Expression<IntValue>.greaterThanOrEqual(compareTo: Int): Expression<Boolean> = value.greaterThanOrEqual(compareTo)
infix fun Expression<IntValue>.lessThan(compareTo: Int): Expression<Boolean> = value.lessThan(compareTo)
infix fun Expression<IntValue>.lessThanOrEqual(compareTo: Int): Expression<Boolean> = value.lessThanOrEqual(compareTo)
// IntValue compareTo expression
@JvmName("greaterThanIntValue")
infix fun Expression<IntValue>.greaterThan(compareTo: Expression<IntValue>): Expression<Boolean> = value.greaterThan(compareTo.value)
@JvmName("greaterThanOrEqualIntValue")
infix fun Expression<IntValue>.greaterThanOrEqual(compareTo: Expression<IntValue>): Expression<Boolean> = value.greaterThanOrEqual(compareTo.value)
@JvmName("lessThanIntValue")
infix fun Expression<IntValue>.lessThan(compareTo: Expression<IntValue>): Expression<Boolean> = value.lessThan(compareTo.value)
@JvmName("lessThanOrEqualIntValue")
infix fun Expression<IntValue>.lessThanOrEqual(compareTo: Expression<IntValue>): Expression<Boolean> = value.lessThanOrEqual(compareTo.value)

// IntValue equals literal
infix fun Expression<IntValue>.equalTo(compareTo: Int): Expression<Boolean> = value.equalTo(compareTo)
infix fun Expression<IntValue>.notEqualTo(compareTo: Int): Expression<Boolean> = value.notEqualTo(compareTo)