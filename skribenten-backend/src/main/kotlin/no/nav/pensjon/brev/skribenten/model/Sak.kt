package no.nav.pensjon.brev.skribenten.model

@JvmInline
value class SaksId(val id: Long) {
    override fun toString() = id.toString()
}