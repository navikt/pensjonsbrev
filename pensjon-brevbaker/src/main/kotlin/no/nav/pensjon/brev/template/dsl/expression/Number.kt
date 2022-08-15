package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.maler.fraser.common.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.expression.*

fun Expression<Double>.format() =
    Expression.BinaryInvoke(
        this,
        Expression.FromScope(ExpressionScope<Any, *>::language),
        BinaryOperation.LocalizedDoubleFormat,
    )

@JvmName("formatInt")
fun Expression<Int>.format() =
    Expression.BinaryInvoke(
        this,
        Expression.FromScope(ExpressionScope<Any, *>::language),
        BinaryOperation.LocalizedIntFormat,
    )

@JvmName("formatDoubleValue")
fun Expression<DoubleValue>.format() =
    select(DoubleValue::value).format()

private val intValueSelector = object : TemplateModelSelector<IntValue, Int> {
    override val className: String = "no.nav.pensjon.brev.api.model.IntValue"
    override val propertyName: String = "value"
    override val propertyType: String = "kotlin.Int"
    override val selector = IntValue::value
}

val Expression<IntValue>.value: Expression<Int>
    get() = Expression.UnaryInvoke(
        this,
        UnaryOperation.Select2(intValueSelector)
    )

fun <T: Comparable<T>> Expression<T>.greaterThan(compareTo: T): Expression<Boolean> =
    Expression.BinaryInvoke(
        first = Comparison.GreaterThan(compareTo).expr(),
        second = this,
        operation = BinaryOperation.ValidatePredicate(),
    )

fun <T: Comparable<T>> Expression<T>.greaterThanOrEqual(compareTo: T): Expression<Boolean> =
    Expression.BinaryInvoke(
        first = Comparison.GreaterThanOrEqual(compareTo).expr(),
        second = this,
        operation = BinaryOperation.ValidatePredicate(),
    )

fun <T: Comparable<T>> Expression<T>.lessThanOrEqual(compareTo: T): Expression<Boolean> =
    Expression.BinaryInvoke(
        first = Comparison.LessThanOrEqual(compareTo).expr(),
        second = this,
        operation = BinaryOperation.ValidatePredicate(),
    )

fun <T: Comparable<T>> Expression<T>.lessThan(compareTo: T): Expression<Boolean> =
    Expression.BinaryInvoke(
        first = Comparison.LessThan(compareTo).expr(),
        second = this,
        operation = BinaryOperation.ValidatePredicate(),
    )

fun <T : IntValue> Expression<T>.greaterThan(compareTo: Int): Expression<Boolean> =
    this.value.greaterThan(compareTo)

fun <T : IntValue> Expression<T>.greaterThanOrEqual(compareTo: Int): Expression<Boolean> =
    this.value.greaterThanOrEqual(compareTo)

fun <T : IntValue> Expression<T>.lessThan(compareTo: Int): Expression<Boolean> =
    this.value.lessThan(compareTo)

fun <T : IntValue> Expression<T>.lessThanOrEqual(compareTo: Int): Expression<Boolean> =
    this.value.lessThanOrEqual(compareTo)

fun <T : IntValue> Expression<T>.equalTo(compareTo: Int): Expression<Boolean> =
    this.value.equalTo(compareTo)

fun <T : IntValue> Expression<T>.notEqualTo(compareTo: Int): Expression<Boolean> =
    this.value.notEqualTo(compareTo)