package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

import no.nav.pensjon.brevbaker.api.model.Kroner

data class BeregningsgrunnlagOrdinar(
    val antallarover1g: Int?,
    val antallarinntektiavtaleland: Int?,
    val beregningsgrunnlagordinerarsbelop: Kroner?,
    val opptjeningutliste: List<OpptjeningUT>?,
)
