package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

import no.nav.pensjon.brevbaker.api.model.Kroner

data class OpptjeningUT(
    val forstegansgstjeneste: Int?,
    val inntektiavtaleland: Boolean?,
    val aar: Int?,
    val pgi: Kroner?,
    val omsorgsaar: Boolean?,
    val justertbelop: Kroner?,
    val avkortetbelop: Kroner?,
    val brukt: Boolean?,
)
