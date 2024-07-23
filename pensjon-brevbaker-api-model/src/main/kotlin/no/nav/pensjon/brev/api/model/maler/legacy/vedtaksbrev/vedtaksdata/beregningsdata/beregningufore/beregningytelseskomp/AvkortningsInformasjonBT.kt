package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

import no.nav.pensjon.brevbaker.api.model.Kroner


data class AvkortningsInformasjonBT(
    val JusteringsbelopPerAr: Kroner?,
    val AvkortingsbelopPerAr: Kroner?,
    val FribelopPeriodisert: Boolean?,
    val InntektPeriodisert: Boolean?,
    val Inntektstak: Kroner?,
)