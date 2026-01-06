package no.nav.pensjon.brev.skribenten.db

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import kotlin.reflect.KProperty

class ValueClassWrapper<Wrapped : Any, Unwrapped: Any>(private val column: Column<Unwrapped>, private val wrap: (Unwrapped) -> Wrapped, private val unwrap: (Wrapped) -> Unwrapped) {
    operator fun <ID : Comparable<ID>> setValue(thisRef: Entity<ID>, property: KProperty<*>, value: Wrapped) {
        with(thisRef) {
            column.setValue(thisRef, property, unwrap(value))
        }
    }

    operator fun <ID : Comparable<ID>> getValue(thisRef: Entity<ID>, property: KProperty<*>): Wrapped {
        return with(thisRef) {
            wrap(column.getValue(thisRef, property))
        }
    }
}

class ValueClassWrapperNullable<Wrapped : Any, Unwrapped: Any>(private val column: Column<Unwrapped?>, private val wrap: (Unwrapped) -> Wrapped, private val unwrap: (Wrapped) -> Unwrapped) {
    operator fun <ID : Comparable<ID>> setValue(thisRef: Entity<ID>, property: KProperty<*>, value: Wrapped?) {
        with(thisRef) {
            column.setValue(thisRef, property, value?.let { unwrap(it) })
        }
    }

    operator fun <ID : Comparable<ID>> getValue(thisRef: Entity<ID>, property: KProperty<*>): Wrapped? {
        return with(thisRef) {
            column.getValue(thisRef, property)?.let(wrap)
        }
    }
}
fun <Unwrapped : Any, Wrapped : Any> Column<Unwrapped>.wrap(wrap: (Unwrapped) -> Wrapped, unwrap: (Wrapped) -> Unwrapped) =
    ValueClassWrapper(this, wrap, unwrap)

fun <Unwrapped : Any, Wrapped : Any> Column<Unwrapped?>.wrap(wrap: (Unwrapped) -> Wrapped, unwrap: (Wrapped) -> Unwrapped) =
    ValueClassWrapperNullable(this, wrap, unwrap)