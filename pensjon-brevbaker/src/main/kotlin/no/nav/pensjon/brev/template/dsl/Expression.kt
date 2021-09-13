package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*
import java.time.LocalDate

fun <Param, Out> argument(parameter: Param): Expression<Out>
        where Param : Parameter,
              Param : ParameterType<Out> =
    Expression.Argument(parameter)

fun Expression<Any>.str(): Expression<String> =
    Expression.UnaryInvoke(this, UnaryOperation.ToString())

fun Expression<LocalDate>.format() =
    Expression.BinaryInvoke(this, Expression.LetterProperty(Letter::language), BinaryOperation.LocalizedDateFormat)

fun <Data: Any, Field> Expression<Data>.select(selector: Data.() -> Field): Expression<Field> =
    Expression.Select(this, selector)

infix fun <T : Comparable<T>> Expression<T>.greaterThan(other: Expression<T>) =
    Expression.BinaryInvoke(this, other, BinaryOperation.GreaterThan())

infix fun <T : Comparable<T>> Expression<T>.greaterThan(other: T) =
    Expression.BinaryInvoke(this, Expression.Literal(other), BinaryOperation.GreaterThan())
