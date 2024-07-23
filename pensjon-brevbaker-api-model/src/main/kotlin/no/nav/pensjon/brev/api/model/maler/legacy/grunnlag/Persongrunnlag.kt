package no.nav.pensjon.brev.api.model.maler.legacy.grunnlag

import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagListeBilateral
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagListeEOS
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagnorge.TrygdetidsgrunnlagListeNor


data class Persongrunnlag(
    val BrukerFlyktning: Boolean?,
    val PersonBostedsland: String?,
    val TrygdetidsgrunnlagListeBilateral: TrygdetidsgrunnlagListeBilateral?,
    val TrygdetidsgrunnlagListeEOS: TrygdetidsgrunnlagListeEOS?,
    val TrygdetidsgrunnlagListeNor: TrygdetidsgrunnlagListeNor?,
)