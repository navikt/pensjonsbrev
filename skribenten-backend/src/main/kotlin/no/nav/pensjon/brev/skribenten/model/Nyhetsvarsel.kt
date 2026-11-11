package no.nav.pensjon.brev.skribenten.model

@JvmInline
value class NyhetsvarselId(val id: String) {
    override fun toString() = id
}

enum class Nyhetsvarselmarkering { SETT_ANNONSEN, GJENNOMFOERT_OMVISNINGEN }
