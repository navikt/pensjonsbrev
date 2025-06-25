package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.StringExpression
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus

@JvmName("quotedStr")
fun String.quoted(): StringExpression = expr().quoted()

@JvmName("quotedExpr")
fun StringExpression.quoted(): StringExpression = QuotationMarks.start + this + QuotationMarks.end
fun quoted(str: String): StringExpression = str.quoted()
fun quoted(str: StringExpression): StringExpression = str.quoted()

object QuotationMarks {
    val start = Expression.UnaryInvoke(Expression.FromScope.Language, UnaryOperation.QuotationStart)
    val end = Expression.UnaryInvoke(Expression.FromScope.Language, UnaryOperation.QuotationEnd)
}
