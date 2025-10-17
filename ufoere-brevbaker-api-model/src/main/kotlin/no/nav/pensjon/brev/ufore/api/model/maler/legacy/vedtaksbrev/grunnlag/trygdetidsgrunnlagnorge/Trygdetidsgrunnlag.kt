package no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagnorge

import java.time.LocalDate

data class Trygdetidsgrunnlag(
    val trygdetidfom: LocalDate?,
    val trygdetidtom: LocalDate?,
)
