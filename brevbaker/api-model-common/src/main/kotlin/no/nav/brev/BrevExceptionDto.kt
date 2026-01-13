package no.nav.brev

import java.util.Objects

class BrevExceptionDto(val tittel: String, val melding: String) {
    override fun equals(other: Any?): Boolean {
        if (other !is BrevExceptionDto) return false
        return tittel == other.tittel && melding == other.melding
    }
    override fun hashCode() = Objects.hash(tittel, melding)
    override fun toString() = "BrevExceptionDto(tittel='$tittel', melding='$melding')"
}