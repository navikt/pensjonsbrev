package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.BinaryOperation
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.ExpressionScope
import java.time.LocalDate

fun Expression<LocalDate>.format() =
    Expression.BinaryInvoke(
        this,
        Expression.FromScope(ExpressionScope<Any, *>::language),
        BinaryOperation.LocalizedDateFormat
    )
