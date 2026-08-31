package no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagListeBilateral
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlageos.TrygdetidsgrunnlagListeEOS
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlagnorge.TrygdetidsgrunnlagListeNor

data class PersongrunnlagAvdod(
    val brukerflyktning: Boolean?,
    val fodselsnummer: String?,
    val trygdetidsgrunnlaglistenor: TrygdetidsgrunnlagListeNor?,
    val trygdetidsgrunnlaglisteeos: TrygdetidsgrunnlagListeEOS?,
    val trygdetidsgrunnlaglistebilateral: TrygdetidsgrunnlagListeBilateral?,
)
