package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

import no.nav.pensjon.brevbaker.api.model.Kroner


data class BarnetilleggFelles(
    val AvkortningsInformasjon: AvkortningsInformasjonBT?,
    val BTFBinnvilget: Boolean?,
    val BTFBnetto: Kroner?,
    val BTFBBrukersInntektTilAvkortning: Kroner?,
    val BTFBInntektBruktiAvkortning: Kroner?,
    val BTFBbelopFratrukketAnnenForeldersInntekt: Kroner?,
    val BTFBfribelop: Kroner?,
    val BTFBinntektAnnenForelder: Kroner?,
)