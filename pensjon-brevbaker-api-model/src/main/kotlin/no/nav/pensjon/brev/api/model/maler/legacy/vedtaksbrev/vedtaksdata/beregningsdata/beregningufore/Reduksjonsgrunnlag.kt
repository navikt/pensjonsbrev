package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore

import no.nav.pensjon.brevbaker.api.model.Kroner

data class Reduksjonsgrunnlag(
    val AndelYtelseAvOIFU: Double?,
    val BarnetilleggRegelverkType: String?,
    val GradertOppjustertIFU: Kroner?,
    val ProsentsatsOIFUForTak: Int?,
)