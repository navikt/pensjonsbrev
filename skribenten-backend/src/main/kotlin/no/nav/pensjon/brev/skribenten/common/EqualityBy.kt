package no.nav.pensjon.brev.skribenten.common

import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.javaGetter

@Suppress("UNCHECKED_CAST")
abstract class EqualityBy<T : EqualityBy<T>>(vararg properties: KProperty1<T, *>) {
    init {
        properties.forEach { prop ->
            require(prop.javaGetter!!.declaringClass.isAssignableFrom(javaClass) ) {
                "Property ${prop.name} is declared on ${prop.javaGetter!!.declaringClass.simpleName}, " +
                        "which is not in the class hierarchy of ${javaClass.simpleName}"
            }
        }
    }
    private val properties = properties.toList()

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return properties.all { it.get(this as T) == it.get(other as T) }
    }

    final override fun hashCode(): Int =
        if (properties.isEmpty()) {
            javaClass.hashCode()
        } else {
            properties.fold(0) { acc, prop -> 31 * acc + (prop.get(this as T)?.hashCode() ?: 0) }
        }
}
