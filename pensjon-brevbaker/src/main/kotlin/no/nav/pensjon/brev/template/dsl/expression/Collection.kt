package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.expression.*

fun <T> Expression<Collection<T>>.containsAll(vararg items: T): Expression<Boolean> =
    Expression.BinaryInvoke(
        first = Collections.ContainsAll(items.toSet()).expr(),
        second = this,
        operation = BinaryOperation.ValidatePredicate(),
    )

fun <T> Expression<Collection<T>>.isEmpty(): Expression<Boolean> =
    Expression.BinaryInvoke(
        first = Collections.IsEmpty.expr(),
        second = this,
        operation = BinaryOperation.ValidatePredicate(),
    )

fun <T> Expression<Collection<T>>.isNotEmpty(): Expression<Boolean> =
    not(this.isEmpty())

/**
 * Collection contains at least one of the listed items.
 */
fun <T> Expression<Collection<T>>.containsAny(vararg items: T): Expression<Boolean> =
    Expression.BinaryInvoke(
        first = Collections.ContainsAny(items.toSet()).expr(),
        second = this,
        operation = BinaryOperation.ValidatePredicate(),
    )

/**
 * Collection exclusively contains the listed items.
 */
fun <T> Expression<Collection<T>>.containsExclusively(body: Collections.ContainsExclusively.Builder<T>.() -> Unit): Expression<Boolean> =
   Expression.BinaryInvoke(
       first = Collections.ContainsExclusively.Builder<T>().apply(body).build().expr(),
       second = this,
       operation = BinaryOperation.ValidatePredicate(),
   )
