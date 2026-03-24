package no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner


data class BarnetilleggFellesYK(
    val belopgammelbtfb: Kroner,
    val belopnybtfb: Kroner,
)