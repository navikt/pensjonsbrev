package no.nav.pensjon.brev.api.model.maler.legacy.personsak

import java.time.LocalDate

data class PersonSak(
    val psfnr: PSfnr,
    val foedselsdato: LocalDate,
)
data class PSfnr(
    val tssid: String,
)
