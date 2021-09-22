package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*
import java.time.LocalDate

fun Expression<Any>.str(): StringExpression =
    Expression.UnaryInvoke(this, UnaryOperation.ToString())

fun Expression<LocalDate>.format() =
    Expression.BinaryInvoke(this, Expression.LetterProperty(Letter<Any>::language), BinaryOperation.LocalizedDateFormat)

fun <Data: Any, Field> Expression<Data>.select(selector: Data.() -> Field, discourageLambdas: Nothing? = null): Expression<Field> =
    Expression.UnaryInvoke(
        this,
        UnaryOperation.Select(selector)
    )

fun StringExpression.concat(expr: StringExpression): StringExpression =
    Expression.BinaryInvoke(
        this,
        expr,
        BinaryOperation.Concat
    )

fun <T> literal(value: T) = Expression.Literal(value)
fun <T> T.expr() = Expression.Literal(this)

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
