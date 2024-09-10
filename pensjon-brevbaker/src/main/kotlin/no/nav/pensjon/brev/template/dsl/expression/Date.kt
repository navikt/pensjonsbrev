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
    val monthSelector = object : TemplateModelSelector<LocalDate, Int> {
        override val className = "java.time.LocalDate"
        override val propertyName = "month"
        override val propertyType = "Int"
        override val selector = LocalDate::getMonthValue
    }

    val daySelector = object : TemplateModelSelector<LocalDate, Int> {
        override val className = "java.time.LocalDate"
        override val propertyName = "day"
        override val propertyType = "Int"
        override val selector = LocalDate::getDayOfMonth
    }

    val firstDayOfYear = object : TemplateModelSelector<LocalDate, LocalDate> {
        override val className = "java.time.LocalDate"
        override val propertyName = "firstDayOfYear"
        override val propertyType = "LocalDate"
        override val selector = {localDate: LocalDate -> localDate.withDayOfYear(1)}
    }

    val lastDayOfYear = object : TemplateModelSelector<LocalDate, LocalDate> {
        override val className = "java.time.LocalDate"
        override val propertyName = "lastDayOfYear"
        override val propertyType = "LocalDate"
        override val selector = {localDate: LocalDate -> localDate.withDayOfYear(localDate.lengthOfYear())}
    }

}


fun Expression<LocalDate?>.legacyGreaterThan(second: Expression<LocalDate?>): Expression<Boolean> =
    Expression.BinaryInvoke(this, second, BinaryOperation.SafeCall(BinaryOperation.GreaterThan())).ifNull(false)

fun Expression<LocalDate?>.legacyGreaterThanOrEqual(second: Expression<LocalDate?>): Expression<Boolean> =
    Expression.BinaryInvoke(this, second, BinaryOperation.SafeCall(BinaryOperation.GreaterThanOrEqual())).ifNull(false)

fun Expression<LocalDate?>.legacyLessThan(second: Expression<LocalDate?>): Expression<Boolean> =
    Expression.BinaryInvoke(this, second, BinaryOperation.SafeCall(BinaryOperation.LessThan())).ifNull(false)

fun Expression<LocalDate?>.legacyLessThanOrEqual(second: Expression<LocalDate?>): Expression<Boolean> =
    Expression.BinaryInvoke(this, second, BinaryOperation.SafeCall(BinaryOperation.LessThanOrEqual())).ifNull(false)

fun Expression<LocalDate?>.legacyGreaterThan(second: LocalDate?): Expression<Boolean> =
    legacyGreaterThan(second.expr())

fun Expression<LocalDate?>.legacyGreaterThanOrEqual(second: LocalDate?): Expression<Boolean> =
    Expression.BinaryInvoke(this, second.expr(), BinaryOperation.SafeCall(BinaryOperation.GreaterThanOrEqual())).ifNull(false)

fun Expression<LocalDate?>.legacyLessThan(second: LocalDate?): Expression<Boolean> =
    Expression.BinaryInvoke(this, second.expr(), BinaryOperation.SafeCall(BinaryOperation.LessThan())).ifNull(false)

fun Expression<LocalDate?>.legacyLessThanOrEqual(second: LocalDate?): Expression<Boolean> =
    Expression.BinaryInvoke(this, second.expr(), BinaryOperation.SafeCall(BinaryOperation.LessThanOrEqual())).ifNull(false)


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