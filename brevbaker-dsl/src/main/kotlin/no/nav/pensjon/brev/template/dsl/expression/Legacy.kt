package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.BinaryOperation
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.ExpressionImpl
import java.time.LocalDate

// Only used to facilitate converted exstream letter functionality
fun Expression<LocalDate?>.legacyGreaterThan(second: Expression<LocalDate?>): Expression<Boolean> =
    ExpressionImpl.BinaryInvokeImpl(this, second, BinaryOperation.SafeCall(BinaryOperation.GreaterThan())).ifNull(false)

fun Expression<LocalDate?>.legacyGreaterThanOrEqual(second: Expression<LocalDate?>): Expression<Boolean> =
    ExpressionImpl.BinaryInvokeImpl(this, second, BinaryOperation.SafeCall(BinaryOperation.GreaterThanOrEqual())).ifNull(false)

fun Expression<LocalDate?>.legacyLessThan(second: Expression<LocalDate?>): Expression<Boolean> =
    ExpressionImpl.BinaryInvokeImpl(this, second, BinaryOperation.SafeCall(BinaryOperation.LessThan())).ifNull(false)

fun Expression<LocalDate?>.legacyLessThanOrEqual(second: Expression<LocalDate?>): Expression<Boolean> =
    ExpressionImpl.BinaryInvokeImpl(this, second, BinaryOperation.SafeCall(BinaryOperation.LessThanOrEqual())).ifNull(false)

fun Expression<LocalDate?>.legacyGreaterThan(second: LocalDate?): Expression<Boolean> =
    legacyGreaterThan(second.expr())

fun Expression<LocalDate?>.legacyGreaterThanOrEqual(second: LocalDate?): Expression<Boolean> =
    ExpressionImpl.BinaryInvokeImpl(this, second.expr(), BinaryOperation.SafeCall(BinaryOperation.GreaterThanOrEqual())).ifNull(false)

fun Expression<LocalDate?>.legacyLessThan(second: LocalDate?): Expression<Boolean> =
    ExpressionImpl.BinaryInvokeImpl(this, second.expr(), BinaryOperation.SafeCall(BinaryOperation.LessThan())).ifNull(false)

fun Expression<LocalDate?>.legacyLessThanOrEqual(second: LocalDate?): Expression<Boolean> =
    ExpressionImpl.BinaryInvokeImpl(this, second.expr(), BinaryOperation.SafeCall(BinaryOperation.LessThanOrEqual())).ifNull(false)