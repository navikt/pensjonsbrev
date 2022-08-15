package no.nav.pensjon.brev.template.dsl.expression

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
