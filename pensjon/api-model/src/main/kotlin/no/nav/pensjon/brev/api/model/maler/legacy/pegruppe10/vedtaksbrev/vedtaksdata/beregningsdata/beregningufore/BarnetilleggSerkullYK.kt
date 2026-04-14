package no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner


data class BarnetilleggSerkullYK(
    val belopgammelbtsb: Kroner?,
    val belopnybtsb: Kroner,
)