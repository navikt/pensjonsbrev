package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.template.Expression

fun <T> Expression<Collection<T>>.containsAll(vararg items: T): Expression<Boolean> =
    map { it.containsAll(items.toSet()) }

fun <T> Expression<Collection<T>>.isEmpty(): Expression<Boolean> =
    map { it.isEmpty() }

/**
 * Collection contains all the listed items in any order, and nothing else.
 */
fun <T> Expression<Collection<T>>.containsOnly(vararg items: T): Expression<Boolean> =
    map { it.toSet() == items.toSet() }

/**
 * Collection contains at least one of the listed items.
 */
fun <T> Expression<Collection<T>>.containsAny(vararg items: T): Expression<Boolean> =
    map { items.any { item -> it.contains(item) } }

/**
 * Collection only contains any of the listed items and at least one,
 * i.e. the collection is a subset of expected.
 */
fun <T> Expression<Collection<T>>.exclusivelyContainsAny(vararg expected: T): Expression<Boolean> =
    map { actual -> actual.all { expected.contains(it) } && expected.any { actual.contains(it) } }

/**
 * Collection only contains items in required and anyOf, contains all required, and at least one of anyOf.
 */
fun <T> Expression<Collection<T>>.exclusivelyContainsRequiredAndAnyOf(required: Set<T>, anyOf: Set<T>): Expression<Boolean> =
    map { actual ->
        (required union anyOf).containsAll(actual)
                && actual.containsAll(required)
                && anyOf.any { actual.contains(it) }
    }