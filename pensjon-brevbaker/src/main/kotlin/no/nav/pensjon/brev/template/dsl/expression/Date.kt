package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.*
import java.time.LocalDate
import java.time.Month

fun Expression<LocalDate>.format(short: Boolean = false) =
    Expression.BinaryInvoke(
        this,
        Expression.FromScope(ExpressionScope<Any, *>::language),
        if(short) BinaryOperation.LocalizedShortDateFormat else BinaryOperation.LocalizedDateFormat
    )

private object LocalDateSelectors {
    val yearSelector = object : TemplateModelSelector<LocalDate, Int> {
        override val className = "java.time.LocalDate"
        override val propertyName = "year"
        override val propertyType = "Int"
        override val selector = LocalDate::getYear
    }

    val monthSelector = object : TemplateModelSelector<LocalDate, Month> {
        override val className = "java.time.LocalDate"
        override val propertyName = "month"
        override val propertyType = "Month"
        override val selector = LocalDate::getMonth
    }

    val dayOfMonthSelector = object : TemplateModelSelector<LocalDate, Int> {
        override val className = "java.time.LocalDate"
        override val propertyName = "month"
        override val propertyType = "Int"
        override val selector = LocalDate::getDayOfMonth
    }
}
val Expression<LocalDate>.year: Expression<Int>
    get() = select(LocalDateSelectors.yearSelector)
val Expression<LocalDate>.month: Expression<Month>
    get() = select(LocalDateSelectors.monthSelector)
val Expression<LocalDate>.day: Expression<Int>
    get() = select(LocalDateSelectors.dayOfMonthSelector)