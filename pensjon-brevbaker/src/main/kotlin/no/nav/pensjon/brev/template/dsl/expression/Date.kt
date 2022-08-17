package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.*
import java.time.LocalDate

fun Expression<LocalDate>.format(short: Boolean = false) =
    Expression.BinaryInvoke(
        this,
        Expression.FromScope(ExpressionScope<Any, *>::language),
        if(short) BinaryOperation.LocalizedShortDateFormat else BinaryOperation.LocalizedDateFormat
    )