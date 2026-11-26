package no.nav.pensjon.brev.brevoppskrift.integrasjoner

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class WorkshopUrl(val value: String) {
    override fun toString(): String = value
}
