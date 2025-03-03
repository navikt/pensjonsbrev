package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.*

fun <Data : Any, Field> Expression<Data>.select(
    selector: TemplateModelSelector<Data, Field>
): Expression<Field> =
    ExpressionImpl.UnaryInvokeImpl(
        this,
        UnaryOperationImpl.Select(selector)
    )

fun <T> T.expr(): Expression<T> = ExpressionImpl.LiteralImpl(this)

fun <T : Any> Expression<T?>.ifNull(then: T): Expression<T> =
    ExpressionImpl.BinaryInvokeImpl(
        this,
        then.expr(),
        BinaryOperationImpl.IfNull(),
    )

fun <T : Any> Expression<T?>.notNull(): Expression<Boolean> = notEqualTo(null)

fun <T : Any> Expression<T?>.isNull(): Expression<Boolean> = equalTo(null)

fun <T : Enum<T>> Expression<Enum<T>>.isOneOf(vararg enums: Enum<T>): Expression<Boolean> = ExpressionImpl.BinaryInvokeImpl(
    this,
    enums.asList().expr(),
    BinaryOperationImpl.EnumInListImpl()
)

fun <T: Any> Expression<List<T>?>.getOrNull(index: Int = 0): Expression<T?> = ExpressionImpl.BinaryInvokeImpl(
    this,
    index.expr(),
    BinaryOperationImpl.GetElementOrNullImpl()
)

fun <T : Enum<T>> Expression<Enum<T>>.isNotAnyOf(vararg enums: Enum<T>): Expression<Boolean> = not(isOneOf(*enums))

@JvmName("notOperator")
operator fun Expression<Boolean>.not(): Expression<Boolean> =
    not(this)

fun not(expr: Expression<Boolean>): Expression<Boolean> =
    ExpressionImpl.UnaryInvokeImpl(expr, UnaryOperationImpl.Not)

operator fun StringExpression.plus(other: StringExpression): Expression.BinaryInvoke<String, String, String> =
    ExpressionImpl.BinaryInvokeImpl(
        this,
        other,
        BinaryOperationImpl.Concat
    )

operator fun StringExpression.plus(other: String): Expression.BinaryInvoke<String, String, String> =
    ExpressionImpl.BinaryInvokeImpl(
        this,
        ExpressionImpl.LiteralImpl(other),
        BinaryOperationImpl.Concat
    )

infix fun Expression<Boolean>.or(other: Expression<Boolean>): Expression.BinaryInvoke<Boolean, Boolean, Boolean> =
    ExpressionImpl.BinaryInvokeImpl(
        this,
        other,
        BinaryOperationImpl.Or
    )

infix fun Expression<Boolean>.and(other: Expression<Boolean>): Expression.BinaryInvoke<Boolean, Boolean, Boolean> =
    ExpressionImpl.BinaryInvokeImpl(
        this,
        other,
        BinaryOperationImpl.And
    )

fun <T> ifElse(condition: Expression<Boolean>, ifTrue: T, ifFalse: T): Expression<T> =
    ExpressionImpl.BinaryInvokeImpl(
        first = condition,
        second = (ifTrue to ifFalse).expr(),
        operation = BinaryOperationImpl.IfElse()
    )

fun <T> ifElse(condition: Expression<Boolean>, ifTrue: Expression<T>, ifFalse: Expression<T>): Expression<T> =
    ExpressionImpl.BinaryInvokeImpl(
        first = condition,
        second = (ifTrue to ifFalse).tuple(),
        operation = BinaryOperationImpl.IfElse(),
    )

fun <T> Pair<Expression<T>, Expression<T>>.tuple(): Expression.BinaryInvoke<T, T, Pair<T, T>> =
    ExpressionImpl.BinaryInvokeImpl(
        first = first,
        second = second,
        operation = BinaryOperationImpl.Tuple()
    )

fun <T> Expression<T>.notEqualTo(other: T): Expression<Boolean> =
    not(equalTo(other))

fun <T> Expression<T>.notEqualTo(other: Expression<T>): Expression<Boolean> =
    not(equalTo(other))

infix fun <T> Expression<T>.equalTo(other: T): Expression.BinaryInvoke<T, T, Boolean> =
    ExpressionImpl.BinaryInvokeImpl(
        first = this,
        second = other.expr(),
        operation = BinaryOperationImpl.EqualImpl()
    )

infix fun <T> Expression<T>.equalTo(other: Expression<T>): Expression.BinaryInvoke<T, T, Boolean> =
    ExpressionImpl.BinaryInvokeImpl(
        first = this,
        second = other,
        operation = BinaryOperationImpl.EqualImpl()
    )

fun <T> Expression<T>.format(formatter: LocalizedFormatter<T>): StringExpression =
    ExpressionImpl.BinaryInvokeImpl(
        first = this,
        second = Expression.FromScope.Language,
        operation = formatter,
    )