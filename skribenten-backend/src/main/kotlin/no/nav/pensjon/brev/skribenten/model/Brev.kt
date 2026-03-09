package no.nav.pensjon.brev.skribenten.model

@JvmInline
value class BrevId(val id: Long) : Comparable<BrevId> {
    override fun compareTo(other: BrevId) = id.compareTo(other.id)
    override fun toString() = id.toString()
}