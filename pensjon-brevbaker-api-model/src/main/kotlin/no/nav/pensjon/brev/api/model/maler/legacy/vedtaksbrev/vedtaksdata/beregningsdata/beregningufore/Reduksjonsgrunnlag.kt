package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore

import no.nav.pensjon.brevbaker.api.model.Kroner

data class Reduksjonsgrunnlag(
    val andelytelseavoifu: Double?,
    val barnetilleggregelverktype: String?,
    val gradertoppjustertifu: Kroner?,
    val prosentsatsoifufortak: Int?,
    val sumbruttoforreduksjonbt: Kroner?,
    val sumbruttoetterreduksjonbt: Kroner?,
    val sumutbt: Kroner?,
)