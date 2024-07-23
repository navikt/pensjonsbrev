package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

data class BeregningYtelsesKomp(
    val UforetrygdOrdiner: UforetrygdOrdiner?,
    val BarnetilleggFelles: BarnetilleggFelles?,
    val BarnetilleggSerkull: BarnetilleggSerkull?,
    val Ektefelletillegg: Ektefelletillegg?,
    val Gjenlevendetillegg: Gjenlevendetillegg?,
)