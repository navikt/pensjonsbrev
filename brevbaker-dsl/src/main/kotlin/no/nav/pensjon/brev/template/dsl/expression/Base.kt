package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.*

fun <Data : Any, Field> Expression<Data>.select(
    selector: TemplateModelSelector<Data, Field>
): Expression<Field> =
    Expression.UnaryInvoke(
        this,
        UnaryOperation.Select(selector)
    )

fun <T> T.expr(): Expression<T> = Expression.Literal(this)

fun <T : Any> Expression<T?>.ifNull(then: T): Expression<T> =
    Expression.BinaryInvoke(
        this,
        then.expr(),
        BinaryOperation.IfNull(),
    )

fun <T : Any> Expression<T?>.notNull(): Expression<Boolean> = notEqualTo(null)

fun <T : Enum<T>> Expression<Enum<T>>.isOneOf(vararg enums: Enum<T>): Expression<Boolean> = Expression.BinaryInvoke(
    this,
    enums.asList().expr(),
    BinaryOperation.EnumInList()
)

fun <T: Any> Expression<List<T>?>.getOrNull(index: Int = 0): Expression<T?> = Expression.BinaryInvoke(
    this,
    index.expr(),
    BinaryOperation.GetElementOrNull()
)

fun <T : Enum<T>> Expression<Enum<T>>.isNotAnyOf(vararg enums: Enum<T>): Expression<Boolean> = not(isOneOf(*enums))

@JvmName("notOperator")
operator fun Expression<Boolean>.not(): Expression<Boolean> =
    not(this)

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

fun <T> Expression<T>.notEqualTo(other: T) =
    not(equalTo(other))

fun <T> Expression<T>.notEqualTo(other: Expression<T>) =
    not(equalTo(other))

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

fun <T> Expression<T>.format(formatter: LocalizedFormatter<T>): StringExpression =
    Expression.BinaryInvoke(
        first = this,
        second = Expression.FromScope.Language,
        operation = formatter,
    )