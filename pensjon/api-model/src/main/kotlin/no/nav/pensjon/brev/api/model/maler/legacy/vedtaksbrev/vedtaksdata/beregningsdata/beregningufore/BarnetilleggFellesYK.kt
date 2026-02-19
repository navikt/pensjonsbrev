package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore

import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner


data class BarnetilleggFellesYK(
    val belopgammelbtfb: Kroner,
    val belopnybtfb: Kroner,
)