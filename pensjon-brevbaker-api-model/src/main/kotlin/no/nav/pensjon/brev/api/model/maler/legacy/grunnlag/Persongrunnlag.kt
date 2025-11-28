package no.nav.pensjon.brev.api.model.maler.legacy.grunnlag

import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.instopphfasteutgifterperiode.InstOpphFasteUtgifterPeriodeListe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.instopphreduksjonsperiode.InstOpphReduksjonsPeriodeListe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagListeBilateral
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlageos.TrygdetidsgrunnlagListeEOS
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagnorge.TrygdetidsgrunnlagListeNor
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor.UforetrygdEtteroppgjor


data class Persongrunnlag(
    val brukerflyktning: Boolean?,
    val personbostedsland: String?,
    val trygdeavtaler: Trygdeavtaler?,
    val trygdetidsgrunnlaglistebilateral: TrygdetidsgrunnlagListeBilateral?,
    val trygdetidsgrunnlaglisteeos: TrygdetidsgrunnlagListeEOS?,
    val trygdetidsgrunnlaglistenor: TrygdetidsgrunnlagListeNor?,
    val uforetrygdetteroppgjor: UforetrygdEtteroppgjor?,
    val instopphfasteutgifterperiodeliste: InstOpphFasteUtgifterPeriodeListe?,
    val instopphreduksjonsperiodeliste: InstOpphReduksjonsPeriodeListe?
)