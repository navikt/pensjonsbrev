package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.BinaryOperation
import no.nav.pensjon.brev.template.Expression
import java.time.LocalDate

// Only used to facilitate converted exstream letter functionality
fun Expression<LocalDate?>.legacyGreaterThan(second: Expression<LocalDate?>): Expression<Boolean> =
    BinaryOperation.SafeCall(BinaryOperation.GreaterThan<LocalDate>())
        .invoke(this, second)
        .ifNull(false)

fun Expression<LocalDate?>.legacyGreaterThanOrEqual(second: Expression<LocalDate?>): Expression<Boolean> =
    BinaryOperation.SafeCall(BinaryOperation.GreaterThanOrEqual<LocalDate>())
        .invoke(this, second)
        .ifNull(false)

fun Expression<LocalDate?>.legacyLessThan(second: Expression<LocalDate?>): Expression<Boolean> =
    BinaryOperation.SafeCall(BinaryOperation.LessThan<LocalDate>())
        .invoke(this, second)
        .ifNull(false)

fun Expression<LocalDate?>.legacyLessThanOrEqual(second: Expression<LocalDate?>): Expression<Boolean> =
    BinaryOperation.SafeCall(BinaryOperation.LessThanOrEqual<LocalDate>())
        .invoke(this, second)
        .ifNull(false)

fun Expression<LocalDate?>.legacyGreaterThan(second: LocalDate?): Expression<Boolean> =
    legacyGreaterThan(second.expr())

fun Expression<LocalDate?>.legacyGreaterThanOrEqual(second: LocalDate?): Expression<Boolean> =
    legacyGreaterThanOrEqual(second.expr())

fun Expression<LocalDate?>.legacyLessThan(second: LocalDate?): Expression<Boolean> =
    legacyLessThan(second.expr())

fun Expression<LocalDate?>.legacyLessThanOrEqual(second: LocalDate?): Expression<Boolean> =
    legacyLessThanOrEqual(second.expr())
