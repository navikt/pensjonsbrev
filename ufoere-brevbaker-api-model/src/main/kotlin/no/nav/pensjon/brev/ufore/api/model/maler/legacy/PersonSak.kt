package no.nav.pensjon.brev.ufore.api.model.maler.legacy

import java.time.LocalDate

data class PersonSak(
    val psfnr: PSfnr,
    val foedselsdato: LocalDate,
)
data class PSfnr(
    val tssid: String,
)
