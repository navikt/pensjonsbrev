package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.maler.fraser.common.*
import no.nav.pensjon.brev.template.*

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

@JvmName("formatIntValue")
fun Expression<IntValue>.format() =
    select(IntValue::value).format()