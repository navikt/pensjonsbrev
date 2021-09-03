package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*

fun <Param, Out> argument(parameter: Param): Expression<Out>
        where Param : Parameter,
              Param : ParameterType<Out> =
    Expression.Argument(parameter)

fun Expression<Any>.str(): Expression<String> = Expression.UnaryInvoke(this, UnaryOperation.ToString())

fun <Data: Any, Field> select(selector: Data.() -> Field, data: Expression<Data>): Expression<Field> =
    Expression.Select(data, selector)

infix fun <T : Comparable<T>> Expression<T>.greaterThan(other: Expression<T>) =
    Expression.BinaryInvoke(this, other, BinaryOperation.GreaterThan())

infix fun <T : Comparable<T>> Expression<T>.greaterThan(other: T) =
    Expression.BinaryInvoke(this, Expression.Literal(other), BinaryOperation.GreaterThan())
