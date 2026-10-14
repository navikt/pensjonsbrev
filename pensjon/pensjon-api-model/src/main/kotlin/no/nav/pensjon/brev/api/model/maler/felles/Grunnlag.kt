package no.nav.pensjon.brev.api.model.maler.felles

import java.time.LocalDate

data class Grunnlag(
    val borIUtlandet: Boolean,
    val anvendtSivilstand: String,//TODO: se over denne og endre til riktig type
)