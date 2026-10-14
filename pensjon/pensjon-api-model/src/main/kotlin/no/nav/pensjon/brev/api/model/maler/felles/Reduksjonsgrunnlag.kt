package no.nav.pensjon.brev.api.model.maler.felles

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

//TODO: verifisert i prod at ingen er null
data class Reduksjonsgrunnlag(
    val sumUtBt: Kroner,// vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumutbt
    val gradertOppjustertIFU: Kroner,// vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu
    val barnetilleggRegelverkType: BarnetilleggRegelverkTypeCode,//vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype
    val sumBruttoFoerReduksjonBt: Kroner,//vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt
    val sumBruttoEtterReduksjonBt: Kroner,//vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt
    val andelYtelseAvOIFU: Double//vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_andelytelseavoifu
) {
    enum class BarnetilleggRegelverkTypeCode {
        NYE_REGLER_2016, OVERGANGSREGLER_2016, INGEN_TAK_JULI2022
    }
}