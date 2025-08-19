package no.nav.pensjon.brev.skribenten.model

object Pdl {
    enum class Gradering {
        FORTROLIG,
        STRENGT_FORTROLIG,
        STRENGT_FORTROLIG_UTLAND,
        UGRADERT,
    }

    enum class Behandlingsnummer {
        B222,
        B255,
        B280,
        B359,
    }
}