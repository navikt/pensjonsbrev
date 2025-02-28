package no.nav.pensjon.brev.template

import kotlin.math.absoluteValue

abstract class AbstractOperation : Operation {
    // Since most operations don't have fields, and hence can't be data classes,
    // we override equals+hashCode to compare by class.
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int = stableHashCode()
    override fun toString(): String = "${this::class.simpleName}"
}