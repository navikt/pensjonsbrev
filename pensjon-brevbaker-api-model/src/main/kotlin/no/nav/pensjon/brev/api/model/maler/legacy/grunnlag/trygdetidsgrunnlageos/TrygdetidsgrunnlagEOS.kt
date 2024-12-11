package no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlageos

import java.time.LocalDate

data class TrygdetidsgrunnlagEOS(
    val trygdetideosland: String?,
    val trygdetidfomeos: LocalDate?,
    val trygdetidtomeos: LocalDate?,
)
