package no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral

import java.time.LocalDate

data class TrygdetidsgrunnlagEOS(
    val TrygdetidEOSLand: String?,
    val TrygdetidFomEOS: LocalDate?,
    val TrygdetidTomEOS: LocalDate?,
)
