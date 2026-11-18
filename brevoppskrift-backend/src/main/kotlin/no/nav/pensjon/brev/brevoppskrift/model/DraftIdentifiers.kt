package no.nav.pensjon.brev.brevoppskrift.model

import java.util.UUID

@JvmInline
value class DraftId(val value: UUID) {
    override fun toString(): String = value.toString()
}

@JvmInline
value class DraftRevision(val value: Long) {
    override fun toString(): String = value.toString()
}
