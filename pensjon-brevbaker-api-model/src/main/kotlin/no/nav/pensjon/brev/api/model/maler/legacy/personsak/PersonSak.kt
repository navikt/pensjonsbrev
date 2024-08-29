package no.nav.pensjon.brev.api.model.maler.legacy.personsak

data class PersonSak(
    val psfnr: PSfnr
)
data class PSfnr(
    val tssid: String,
)
