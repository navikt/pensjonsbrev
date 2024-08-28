package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

import no.nav.pensjon.brevbaker.api.model.Kroner


data class AvkortningsInformasjon(
    val oifu: Kroner?,
    val oieu: Kroner?,
    val belopsgrense: Kroner?,
    val inntektsgrense: Kroner?,
    val inntektstak: Kroner?,
    val ugradertbruttoperar: Kroner?,
    val kompensasjonsgrad: Double?,
    val utbetalingsgrad: Int?,
    val forventetinntekt: Kroner?,
    val inntektsgrensenestear: Kroner?,
)
