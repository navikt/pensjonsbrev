package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.UnaryOperation

fun <Data : Any, Field> Expression<Data>.select(
    selector: TemplateModelSelector<Data, Field>
): Expression<Field> =
    UnaryOperation.Select(selector).invoke(this)

@JvmName("selectOrNull")
fun <Data : Any, Field> Expression<Data?>.select(
    selector: TemplateModelSelector<Data, Field>
) = safe(UnaryOperation.Select(selector))

fun <T> T.expr(): Expression<T> = Expression.Literal(this)

fun <T : Any> Expression<T?>.ifNull(then: Expression<T>): Expression<T> =
    BinaryOperation.IfNull<T>().invoke(this, then)

fun <T : Any> Expression<T?>.ifNull(then: T): Expression<T> =
    ifNull(then.expr())

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

@JvmName("formatNullable")
fun <T> Expression<T?>.format(formatter: LocalizedFormatter<T>): Expression<String?> =
    safe(formatter, Expression.FromScope.Language)

fun <In : Any, Out> Expression<In?>.safe(operation: UnaryOperation<In, Out>): Expression<Out?> =
    UnaryOperation.SafeCall(operation).invoke(this)

fun <In1 : Any, In2, Out> Expression<In1?>.safe(
    operation: BinaryOperation<In1, In2 & Any, Out>,
    other: Expression<In2>
): Expression<Out?> =
    Expression.BinaryInvoke(this, other, BinaryOperation.SafeCall(operation))

fun <In : Any, Out> Expression<In?>.safe(block: Expression<In>.() -> Expression<Out>): Expression<Out?> =
    Expression.NullSafeApplication(this, block)

