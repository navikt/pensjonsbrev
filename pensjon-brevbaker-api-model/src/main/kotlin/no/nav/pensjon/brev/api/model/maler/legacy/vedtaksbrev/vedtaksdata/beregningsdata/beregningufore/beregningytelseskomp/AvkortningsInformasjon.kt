package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

import no.nav.pensjon.brevbaker.api.model.Kroner


data class AvkortningsInformasjon(
    val Oifu: Kroner?,
    val Oieu: Kroner?,
    val Belopsgrense: Kroner?,
    val Inntektsgrense: Kroner?,
    val Inntektstak: Kroner?,
    val UgradertBruttoPerAr: Kroner?,
    val Kompensasjonsgrad: Double?,
    val Utbetalingsgrad: Int?,
    val ForventetInntekt: Kroner?,
    val InntektsgrenseNesteAr: Kroner?,
)
