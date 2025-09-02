package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.UnaryOperation

interface QuotationMarks {
    val start: String
    val end: String

    object BokmalNynorsk : QuotationMarks {
        override val start = "«"
        override val end = "»"
    }
    object English : QuotationMarks {
        override val start = "'"
        override val end = "'"
    }

    companion object {
        val start = Expression.UnaryInvoke(Expression.FromScope.Language, UnaryOperation.QuotationStart)
        val end = Expression.UnaryInvoke(Expression.FromScope.Language, UnaryOperation.QuotationEnd)
    }
}