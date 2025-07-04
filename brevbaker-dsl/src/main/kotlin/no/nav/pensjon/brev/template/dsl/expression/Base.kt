package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.*

fun <Data : Any, Field> Expression<Data>.select(
    selector: TemplateModelSelector<Data, Field>
): Expression<Field> =
    UnaryOperation.Select(selector).invoke(this)

fun <T> T.expr(): Expression<T> = Expression.Literal(this)

fun <T : Any> Expression<T?>.ifNull(then: T): Expression<T> =
    BinaryOperation.IfNull<T>().invoke(this, then.expr())

fun <T : Any> Expression<T?>.notNull(): Expression<Boolean> = notEqualTo(null)

fun <T : Any> Expression<T?>.isNull(): Expression<Boolean> = equalTo(null)

fun <T : Enum<T>> Expression<Enum<T>>.isOneOf(vararg enums: Enum<T>): Expression<Boolean> =
    BinaryOperation.EnumInList<Enum<T>>().invoke(this, enums.asList().expr())

fun <T : Any> Expression<List<T>?>.getOrNull(index: Int = 0): Expression<T?> =
    BinaryOperation.GetElementOrNull<T>().invoke(this, index.expr())

fun <T : Enum<T>> Expression<Enum<T>>.isNotAnyOf(vararg enums: Enum<T>): Expression<Boolean> =
    not(isOneOf(*enums))

@JvmName("notOperator")
operator fun Expression<Boolean>.not(): Expression<Boolean> =
    not(this)

fun not(expr: Expression<Boolean>): Expression<Boolean> =
    UnaryOperation.Not(expr)

operator fun StringExpression.plus(other: StringExpression) =
    BinaryOperation.Concat(this, other)

operator fun StringExpression.plus(other: String): Expression<String> =
    this + other.expr()

infix fun Expression<Boolean>.or(other: Expression<Boolean>): Expression<Boolean> =
    BinaryOperation.Or(this, other)

infix fun Expression<Boolean>.and(other: Expression<Boolean>): Expression<Boolean> =
    BinaryOperation.And(this, other)

fun <T> ifElse(condition: Expression<Boolean>, ifTrue: T, ifFalse: T): Expression<T> =
    ifElse(condition, ifTrue.expr(), ifFalse.expr())

fun <T> ifElse(condition: Expression<Boolean>, ifTrue: Expression<T>, ifFalse: Expression<T>): Expression<T> =
    BinaryOperation.IfElse<T>().invoke(condition, (ifTrue to ifFalse).tuple())

fun <In1, In2> Pair<Expression<In1>, Expression<In2>>.tuple(): Expression<Pair<In1, In2>> =
    BinaryOperation.Tuple<In1, In2>().invoke(first, second)

fun <T> Expression<T>.notEqualTo(other: T): Expression<Boolean> =
    not(equalTo(other))

fun <T> Expression<T>.notEqualTo(other: Expression<T>): Expression<Boolean> =
    not(equalTo(other))

infix fun <T> Expression<T>.equalTo(other: T): Expression<Boolean> =
    equalTo(other.expr())

infix fun <T> Expression<T>.equalTo(other: Expression<T>): Expression<Boolean> =
    BinaryOperation.Equal<T>().invoke(this, other)

fun <T> Expression<T>.format(formatter: LocalizedFormatter<T>): StringExpression =
    formatter(this, Expression.FromScope.Language)
