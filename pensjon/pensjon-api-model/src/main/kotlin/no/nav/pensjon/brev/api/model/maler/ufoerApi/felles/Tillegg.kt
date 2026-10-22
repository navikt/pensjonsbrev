package no.nav.pensjon.brev.api.model.maler.ufoerApi.felles

data class Tillegg(
    val ektefelletilleggInnvilget: Boolean,//vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget
    val gjenlevendetilleggInnvilget: Boolean,//vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget
    val nyttGjenlevendetillegg: Boolean,
)