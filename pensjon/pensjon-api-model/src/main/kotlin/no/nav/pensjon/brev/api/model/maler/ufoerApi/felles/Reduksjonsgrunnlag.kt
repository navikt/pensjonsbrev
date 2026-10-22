package no.nav.pensjon.brev.api.model.maler.ufoerApi.felles

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

data class Reduksjonsgrunnlag(
    val sumUtBt: Kroner,
    val gradertOppjustertIFU: Kroner,
    val barnetilleggRegelverkType: BarnetilleggRegelverkTypeCode,
    val sumBruttoFoerReduksjonBt: Kroner,
    val sumBruttoEtterReduksjonBt: Kroner,
    val andelYtelseAvOIFU: Double
) {
    enum class BarnetilleggRegelverkTypeCode {
        NYE_REGLER_2016, OVERGANGSREGLER_2016, INGEN_TAK_JULI2022
    }
}