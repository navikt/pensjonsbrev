package no.nav.pensjon.brev.skribenten.model

import java.time.LocalDate

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
        B345,
        B296,
        B298,
        B150,
        B377,
        B300,
    }

    data class PersonContext(
        val adressebeskyttelse: Boolean,
        val doedsdato: LocalDate?,
    )
}