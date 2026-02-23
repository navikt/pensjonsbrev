package no.nav.pensjon.brev.skribenten.model

@JvmInline
value class NavIdent(val id: String) {
    override fun toString() = id
}