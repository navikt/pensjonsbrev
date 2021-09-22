package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*
import java.time.LocalDate

fun Expression<Any>.str(): Expression<String> =
    Expression.UnaryInvoke(this, UnaryOperation.ToString())

fun Expression<LocalDate>.format() =
    Expression.BinaryInvoke(this, Expression.LetterProperty(Letter<Any>::language), BinaryOperation.LocalizedDateFormat)

fun <Data: Any, Field> Expression<Data>.select(selector: Data.() -> Field, discourageLambdas: Nothing? = null): Expression<Field> =
    Expression.UnaryInvoke(
        this,
        UnaryOperation.Select(selector)
    )
