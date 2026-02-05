package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

data class BeregningYtelsesKomp(
    val uforetrygdordiner: UforetrygdOrdiner?,
    val barnetilleggfelles: BarnetilleggFelles?,
    val barnetilleggserkull: BarnetilleggSerkull?,
    val ektefellegarantitillegg: Ektefellegarantitillegg?,
    val ektefelletillegg: Ektefelletillegg?,
    val gjenlevendetillegg: Gjenlevendetillegg?,
)