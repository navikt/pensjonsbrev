package no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlageos

import java.time.LocalDate

data class TrygdetidsgrunnlagEOS(
    val trygdetideosland: String?,
    val trygdetidfomeos: LocalDate?,
    val trygdetidtomeos: LocalDate?,
)
