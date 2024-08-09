package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

import no.nav.pensjon.brevbaker.api.model.Kroner


data class BarnetilleggSerkull(
    val avkortningsinformasjon: AvkortningsInformasjonBT?,
    val btsbinnvilget: Boolean?,
    val btsbnetto: Kroner?,
    val btsbinntektbruktiavkortning: Kroner?,
    val btsbfribelop: Kroner?,
    val antallbarnserkull: Int?,
    )