package no.nav.pensjon.brev.api.model.vedlegg

import java.time.LocalDate

data class Trygdetid(
    val fom: LocalDate?,
    val tom: LocalDate?,
    val land: String?,
)
