package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore

import no.nav.pensjon.brevbaker.api.model.Kroner

data class UforetrygdOrdinerYK(
    val belopgammelut: Kroner?,
    val belopnyut: Kroner?,
)
