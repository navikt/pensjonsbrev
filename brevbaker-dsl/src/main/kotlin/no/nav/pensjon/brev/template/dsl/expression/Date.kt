package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.*
import java.time.LocalDate

fun Expression<LocalDate>.format(short: Boolean = false) =
    format(formatter = if (short) LocalizedFormatter.ShortDateFormat else LocalizedFormatter.DateFormat)

fun Expression<LocalDate>.formatMonthYear(): Expression<String> = this.format(LocalizedFormatter.MonthYearFormatter)

private object LocalDateSelectors {
    val yearSelector =
        object : TemplateModelSelector<LocalDate, Int> {
            override val className = "java.time.LocalDate"
            override val propertyName = "year"
            override val propertyType = "Int"
            override val selector = LocalDate::getYear
        }
    val monthSelector =
        object : TemplateModelSelector<LocalDate, Int> {
            override val className = "java.time.LocalDate"
            override val propertyName = "month"
            override val propertyType = "Int"
            override val selector = LocalDate::getMonthValue
        }

    val daySelector =
        object : TemplateModelSelector<LocalDate, Int> {
            override val className = "java.time.LocalDate"
            override val propertyName = "day"
            override val propertyType = "Int"
            override val selector = LocalDate::getDayOfMonth
        }

    val firstDayOfYear =
        object : TemplateModelSelector<LocalDate, LocalDate> {
            override val className = "java.time.LocalDate"
            override val propertyName = "firstDayOfYear"
            override val propertyType = "LocalDate"
            override val selector = { localDate: LocalDate -> localDate.withDayOfYear(1) }
        }

    val lastDayOfYear =
        object : TemplateModelSelector<LocalDate, LocalDate> {
            override val className = "java.time.LocalDate"
            override val propertyName = "lastDayOfYear"
            override val propertyType = "LocalDate"
            override val selector = { localDate: LocalDate -> localDate.withDayOfYear(localDate.lengthOfYear()) }
        }
}

val Expression<LocalDate>.year: Expression<Int>
    get() = select(LocalDateSelectors.yearSelector)

val Expression<LocalDate>.month: Expression<Int>
    get() = select(LocalDateSelectors.monthSelector)

val Expression<LocalDate>.day: Expression<Int>
    get() = select(LocalDateSelectors.daySelector)

val Expression<LocalDate>.firstDayOfYear: Expression<LocalDate>
    get() = select(LocalDateSelectors.firstDayOfYear)

val Expression<LocalDate>.lastDayOfYear: Expression<LocalDate>
    get() = select(LocalDateSelectors.lastDayOfYear)
