package no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral

import java.time.LocalDate

data class TrygdetidsgrunnlagBilateral(
    val trygdetidbilateralland: String?,
    val trygdetidfombilateral: LocalDate?,
    val trygdetidtombilateral: LocalDate?,
)
