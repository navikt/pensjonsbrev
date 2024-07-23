package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

import no.nav.pensjon.brevbaker.api.model.Kroner


data class BarnetilleggSerkull(
    val AvkortningsInformasjon: AvkortningsInformasjonBT?,
    val BTSBinnvilget: Boolean?,
    val BTSBnetto: Kroner?,
    val BTSBInntektBruktiAvkortning: Kroner?,
    val BTSBfribelop: Kroner?,
)