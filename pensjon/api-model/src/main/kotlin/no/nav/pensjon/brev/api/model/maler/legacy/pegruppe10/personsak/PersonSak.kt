package no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.personsak

import java.time.LocalDate

data class PersonSak(
    val psfnr: PSfnr,
    val foedselsdato: LocalDate,
)
data class PSfnr(
    val tssid: String,
)
