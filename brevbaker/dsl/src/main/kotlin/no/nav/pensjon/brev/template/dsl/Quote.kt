package no.nav.pensjon.brev.template.dsl

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
}