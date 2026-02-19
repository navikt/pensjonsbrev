package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner


data class AvkortningsInformasjonBT(
    val justeringsbelopperar: Kroner?,
    val avkortingsbelopperar: Kroner?,
    val fribelopperiodisert: Boolean?,
    val inntektperiodisert: Boolean?,
    val inntektstak: Kroner?,
)