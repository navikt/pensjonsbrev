package no.nav.pensjon.brev.skribenten.model

@JvmInline
value class JournalpostId(val id: Long) {
    override fun toString() = id.toString()
}