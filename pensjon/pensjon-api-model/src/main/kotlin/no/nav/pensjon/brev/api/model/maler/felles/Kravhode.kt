package no.nav.pensjon.brev.api.model.maler.felles

import java.time.LocalDate

data class Kravhode(
    val virkFom: LocalDate,
    val mottattDato: LocalDate,
    val arsak: String,
)