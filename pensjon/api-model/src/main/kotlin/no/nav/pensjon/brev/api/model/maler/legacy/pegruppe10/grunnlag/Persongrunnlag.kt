package no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.instopphfasteutgifterperiode.InstOpphFasteUtgifterPeriodeListe
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.instopphreduksjonsperiode.InstOpphReduksjonsPeriodeListe
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagListeBilateral
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlageos.TrygdetidsgrunnlagListeEOS
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlagnorge.TrygdetidsgrunnlagListeNor
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.uforetrygdetteroppgjor.UforetrygdEtteroppgjor


data class Persongrunnlag(
    val brukerflyktning: Boolean?,
    val instopphfasteutgifterperiodeliste: InstOpphFasteUtgifterPeriodeListe? = null,
    val instopphreduksjonsperiodeliste: InstOpphReduksjonsPeriodeListe? = null,
    val personbostedsland: String?,
    val trygdeavtaler: Trygdeavtaler?,
    val trygdetidsgrunnlaglistebilateral: TrygdetidsgrunnlagListeBilateral?,
    val trygdetidsgrunnlaglisteeos: TrygdetidsgrunnlagListeEOS?,
    val trygdetidsgrunnlaglistenor: TrygdetidsgrunnlagListeNor?,
    val uforehistorikkgarantigrad: Int?,
    val uforetrygdetteroppgjor: UforetrygdEtteroppgjor?,
)