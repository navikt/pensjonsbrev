package no.nav.pensjon.brev.skribenten.model

import java.time.LocalDate

object Pdl {
    enum class Gradering {
        FORTROLIG,
        STRENGT_FORTROLIG,
        STRENGT_FORTROLIG_UTLAND,
        UGRADERT,
    }

    data class PersonContext(
        val adressebeskyttelse: Boolean,
        val doedsdato: LocalDate?,
    )
}