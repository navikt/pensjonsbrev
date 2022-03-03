package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.maler.fraser.common.LocalDateValue
import no.nav.pensjon.brev.template.BinaryOperation
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.ExpressionScope
import java.time.LocalDate

fun Expression<LocalDate>.format(short: Boolean = false) =
    Expression.BinaryInvoke(
        this,
        Expression.FromScope(ExpressionScope<Any, *>::language),
        if(short) BinaryOperation.LocalizedShortDateFormat else BinaryOperation.LocalizedDateFormat
    )

@JvmName("formatLocalDateValue")
fun Expression<LocalDateValue>.format() =
    select(LocalDateValue::date).format()