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
        when(short) {
                true -> BinaryOperation.LocalizedShortDateFormat
                false -> BinaryOperation.LocalizedDateFormat
        }//.let { BinaryOperation.NullSafe(it) }
    )

@JvmName("formatLocalDateValue")
fun Expression<LocalDateValue>.format() =
    select(LocalDateValue::date).format()