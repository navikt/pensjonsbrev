package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*
import java.time.LocalDate

fun Expression<Any>.str(): StringExpression =
    Expression.UnaryInvoke(this, UnaryOperation.ToString())

fun Expression<LocalDate>.format() =
    Expression.BinaryInvoke(this, Expression.LetterProperty(ExpressionScope<Any, *>::language), BinaryOperation.LocalizedDateFormat)

fun <Data : Any, Field> Expression<Data>.select(selector: Data.() -> Field, @Suppress("UNUSED_PARAMETER") discourageLambdas: Nothing? = null): Expression<Field> =
    Expression.UnaryInvoke(
        this,
        UnaryOperation.Select(selector)
    )

fun <T> T.expr() = Expression.Literal(this)

fun <T> Expression<T?>.ifNull(then: T) =
    Expression.UnaryInvoke(this, UnaryOperation.IfNull(then))

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
