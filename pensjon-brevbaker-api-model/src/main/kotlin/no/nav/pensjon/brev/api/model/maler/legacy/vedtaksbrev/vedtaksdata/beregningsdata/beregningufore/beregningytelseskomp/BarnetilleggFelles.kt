package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

import no.nav.pensjon.brevbaker.api.model.Kroner


data class BarnetilleggFelles(
    val avkortningsinformasjon: AvkortningsInformasjonBT?,
    val btfbinnvilget: Boolean?,
    val btfbnetto: Kroner?,
    val btfbbrukersinntekttilavkortning: Kroner?,
    val btfbinntektbruktiavkortning: Kroner?,
    val btfbbelopfratrukketannenforeldersinntekt: Kroner?,
    val btfbfribelop: Kroner?,
    val btfbbruttoperar: Kroner?,
    val btfbinntektannenforelder: Kroner?,
    val antallbarnfelles: Int?,
)