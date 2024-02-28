package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.*
import java.time.LocalDate

fun Expression<LocalDate>.format(short: Boolean = false) =
    format(formatter = if(short) LocalizedFormatter.ShortDateFormat else LocalizedFormatter.DateFormat)

private object LocalDateSelectors {
    val yearSelector = object : TemplateModelSelector<LocalDate, Int> {
        override val className = "java.time.LocalDate"
        override val propertyName = "year"
        override val propertyType = "Int"
        override val selector = LocalDate::getYear
    }
}

val Expression<LocalDate>.year: Expression<Int>
    get() = select(LocalDateSelectors.yearSelector)