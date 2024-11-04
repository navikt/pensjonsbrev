package no.nav.pensjon.brev.api.model.maler.legacy.grunnlag

import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagListeBilateral
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlageos.TrygdetidsgrunnlagListeEOS
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagnorge.TrygdetidsgrunnlagListeNor

data class PersongrunnlagAvdod(
    val brukerflyktning: Boolean?,
    val fodselsnummer: String?,
    val trygdetidsgrunnlaglistenor: TrygdetidsgrunnlagListeNor?,
    val trygdetidsgrunnlaglisteeos: TrygdetidsgrunnlagListeEOS?,
    val trygdetidsgrunnlaglistebilateral: TrygdetidsgrunnlagListeBilateral?,
)
