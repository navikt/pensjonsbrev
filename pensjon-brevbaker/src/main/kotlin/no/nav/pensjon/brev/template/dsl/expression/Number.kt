package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.maler.fraser.common.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.expression.*
import no.nav.pensjon.brevbaker.api.model.IntValue

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

fun Expression<Double>.format(): Expression<String> =
    Expression.BinaryInvoke(
        this,
        Expression.FromScope(ExpressionScope<Any, *>::language),
        BinaryOperation.LocalizedDoubleFormat,
    )

@JvmName("formatInt")
fun Expression<Int>.format(): Expression<String> =
    Expression.BinaryInvoke(
        this,
        Expression.FromScope(ExpressionScope<Any, *>::language),
        BinaryOperation.LocalizedIntFormat,
    )


// TODO: Skriv tester på disse
fun <T: Comparable<T>> Expression<T>.greaterThan(compareTo: Expression<T>): Expression<Boolean> =
    Expression.BinaryInvoke(
        first = this,
        second = compareTo,
        operation = BinaryOperation.GreaterThan(),
    )

fun <T: Comparable<T>> Expression<T>.greaterThanOrEqual(compareTo: Expression<T>): Expression<Boolean> =
    Expression.BinaryInvoke(
        first = this,
        second = compareTo,
        operation = BinaryOperation.GreaterThanOrEqual(),
    )

fun <T: Comparable<T>> Expression<T>.lessThanOrEqual(compareTo: Expression<T>): Expression<Boolean> =
    Expression.BinaryInvoke(
        first = this,
        second = compareTo,
        operation = BinaryOperation.LessThanOrEqual(),
    )

fun <T: Comparable<T>> Expression<T>.lessThan(compareTo: Expression<T>): Expression<Boolean> =
    Expression.BinaryInvoke(
        first = this,
        second = compareTo,
        operation = BinaryOperation.LessThan(),
    )

// Literal compareTo value
fun <T: Comparable<T>> Expression<T>.greaterThan(compareTo: T): Expression<Boolean> = greaterThan(compareTo.expr())
fun <T: Comparable<T>> Expression<T>.greaterThanOrEqual(compareTo: T): Expression<Boolean> = greaterThanOrEqual(compareTo.expr())
fun <T: Comparable<T>> Expression<T>.lessThanOrEqual(compareTo: T): Expression<Boolean> = lessThanOrEqual(compareTo.expr())
fun <T: Comparable<T>> Expression<T>.lessThan(compareTo: T): Expression<Boolean> = lessThan(compareTo.expr())
// IntValue compareTo literal
fun Expression<IntValue>.greaterThan(compareTo: Int): Expression<Boolean> = value.greaterThan(compareTo)
fun Expression<IntValue>.greaterThanOrEqual(compareTo: Int): Expression<Boolean> = value.greaterThanOrEqual(compareTo)
fun Expression<IntValue>.lessThan(compareTo: Int): Expression<Boolean> = value.lessThan(compareTo)
fun Expression<IntValue>.lessThanOrEqual(compareTo: Int): Expression<Boolean> = value.lessThanOrEqual(compareTo)
// IntValue compareTo expression
@JvmName("greaterThanIntValue")
fun Expression<IntValue>.greaterThan(compareTo: Expression<IntValue>): Expression<Boolean> = value.greaterThan(compareTo.value)
@JvmName("greaterThanOrEqualIntValue")
fun Expression<IntValue>.greaterThanOrEqual(compareTo: Expression<IntValue>): Expression<Boolean> = value.greaterThanOrEqual(compareTo.value)
@JvmName("lessThanIntValue")
fun Expression<IntValue>.lessThan(compareTo: Expression<IntValue>): Expression<Boolean> = value.lessThan(compareTo.value)
@JvmName("lessThanOrEqualIntValue")
fun Expression<IntValue>.lessThanOrEqual(compareTo: Expression<IntValue>): Expression<Boolean> = value.lessThanOrEqual(compareTo.value)

// IntValue equals literal
fun Expression<IntValue>.equalTo(compareTo: Int): Expression<Boolean> = value.equalTo(compareTo)
fun Expression<IntValue>.notEqualTo(compareTo: Int): Expression<Boolean> = value.notEqualTo(compareTo)