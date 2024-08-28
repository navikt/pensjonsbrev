package no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral

import java.time.LocalDate

data class TrygdetidsgrunnlagEOS(
    val trygdetideosland: String?,
    val trygdetidfomeos: LocalDate?,
    val trygdetidtomeos: LocalDate?,
)
