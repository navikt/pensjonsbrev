package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.api.model.Telefonnummer
import no.nav.pensjon.brev.template.*
import java.time.LocalDate

fun Expression<Any>.str(): StringExpression =
    Expression.UnaryInvoke(this, UnaryOperation.ToString())

fun Expression<LocalDate>.format() =
    Expression.BinaryInvoke(this, Expression.FromScope(ExpressionScope<Any, *>::language), BinaryOperation.LocalizedDateFormat)

fun Expression<Telefonnummer>.format() =
    Expression.UnaryInvoke(this, UnaryOperation.FormatPhoneNumber)

fun <Data : Any, Field> Expression<Data>.select(selector: Data.() -> Field, @Suppress("UNUSED_PARAMETER") discourageLambdas: Nothing? = null): Expression<Field> =
    Expression.UnaryInvoke(
        this,
        UnaryOperation.Select(selector)
    )

fun <T, R> Expression<T>.map(transform: (T) -> R): Expression<R> =
    Expression.UnaryInvoke(
        this,
        UnaryOperation.Select(transform)
    )

fun <T> T.expr() = Expression.Literal(this)

fun <T> Expression<T?>.ifNull(then: T) =
    Expression.UnaryInvoke(this, UnaryOperation.IfNull(then))

fun not(expr: Expression<Boolean>): Expression<Boolean> =
    Expression.UnaryInvoke(expr, UnaryOperation.Not)

operator fun StringExpression.plus(other: StringExpression) =
    Expression.BinaryInvoke(
        this,
        other,
        BinaryOperation.Concat
    )

operator fun StringExpression.plus(other: String) =
    Expression.BinaryInvoke(
        this,
        Expression.Literal(other),
        BinaryOperation.Concat
    )
infix fun Expression<Boolean>.or(other: Expression<Boolean>) =
    Expression.BinaryInvoke(
        this,
        other,
        BinaryOperation.Or
    )
infix fun Expression<Boolean>.and(other: Expression<Boolean>) =
    Expression.BinaryInvoke(
        this,
        other,
        BinaryOperation.And
    )
