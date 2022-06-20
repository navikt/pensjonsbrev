package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.BinaryOperation
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.StringExpression
import no.nav.pensjon.brev.template.UnaryOperation

fun <Data : Any, Field> Expression<Data>.select(
    selector: Data.() -> Field,
    @Suppress("UNUSED_PARAMETER") discourageLambdas: Nothing? = null
): Expression<Field> =
    Expression.UnaryInvoke(
        this,
        UnaryOperation.Select(selector)
    )

fun <T, R> Expression<T>.map(transform: (T) -> R): Expression<R> =
    Expression.UnaryInvoke(
        this,
        UnaryOperation.Select(transform),
    )

fun <T> T.expr() = Expression.Literal(this)

fun <T: Any> Expression<T?>.ifNull(then: T) =
    Expression.UnaryInvoke(this, UnaryOperation.IfNull(then))

fun <T: Any> Expression<T?>.notNull() =
    map { it != null }

fun <T : Enum<T>> Expression<Enum<T>>.isOneOf(vararg enums: Enum<T>): Expression<Boolean> = Expression.BinaryInvoke(
    this,
    enums.asList().expr(),
    BinaryOperation.EnumInList()
)

fun <T : Enum<T>> Expression<Enum<T>>.isNotAnyOf(vararg enums: Enum<T>): Expression<Boolean> = Expression.BinaryInvoke(
    this,
    enums.asList().expr(),
    BinaryOperation.EnumNotInList()
)

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

fun <T> ifElse(condition: Expression<Boolean>, ifTrue: T, ifFalse: T): Expression<T> =
    Expression.BinaryInvoke(
        first = condition,
        second = (ifTrue to ifFalse).expr(),
        operation = BinaryOperation.IfElse()
    )

fun <T> ifElse(condition: Expression<Boolean>, ifTrue: Expression<T>, ifFalse: Expression<T>): Expression<T> =
    Expression.BinaryInvoke(
        first = condition,
        second = (ifTrue to ifFalse).tuple(),
        operation = BinaryOperation.IfElse(),
    )

fun <T> Pair<Expression<T>, Expression<T>>.tuple() =
    Expression.BinaryInvoke(
        first = first,
        second = second,
        operation = BinaryOperation.Tuple()
    )

infix fun <T> Expression<T>.equalTo(other: T) =
    Expression.BinaryInvoke(
        first = this,
        second = other.expr(),
        operation = BinaryOperation.Equal()
    )

infix fun <T> Expression<T>.equalTo(other: Expression<T>) =
    Expression.BinaryInvoke(
        first = this,
        second = other,
        operation = BinaryOperation.Equal()
    )