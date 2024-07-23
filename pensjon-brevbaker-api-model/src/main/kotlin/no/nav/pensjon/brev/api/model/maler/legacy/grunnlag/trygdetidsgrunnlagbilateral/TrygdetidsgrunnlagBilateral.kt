package no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral

import java.time.LocalDate

data class TrygdetidsgrunnlagBilateral(
    val TrygdetidBilateralLand: String?,
    val TrygdetidFomBilateral: LocalDate?,
    val TrygdetidTomBilateral: LocalDate?,
)
