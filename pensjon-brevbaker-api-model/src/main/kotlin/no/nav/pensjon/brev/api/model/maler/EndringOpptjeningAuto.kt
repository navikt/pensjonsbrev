package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class EndringOpptjeningAutoDto(
    val barnetilleggFellesbarn: BarnetilleggFellesbarn?,
    val barnetilleggSaerkullsbarn: BarnetilleggSaerkullsbarn?,
    val endringIOpptjening: EndringIOpptjening,
    val folketrygdloven: Folketrygdloven, // used
    val ufoeretrygd: Ufoeretrygd,  // used
) {

    data class EndringIOpptjening(
        val ufoerBeloepOekt: Boolean,  // used
        val ufoerBeloepRedusert: Boolean,  // used
        val virkningsDato: LocalDate,  // used
    )

    data class Folketrygdloven(
        val harYrkesskadeGradUtbetaling: Boolean, // yrkesskadeGrad > 0  - used
        val innvilgetEktefelletillegg: Boolean,  // used
        val innvilgetFellesbarntillegg: Boolean,  // used
        val innvilgetGjenlevendetillegg: Boolean,  // used
        val innvilgetSaerkullsbarntillegg: Boolean,  // used
    )

    data class Ufoeretrygd(
        val ektefelletilleggUtbeltalt: Kroner?,
        val gjenlevendetilleggUtbetalt: Kroner?,
        val harUtbetalingsgrad: Boolean,
        val ufoertrygdUtbetalt: Kroner,
        val utbetaltPerMaaned: Kroner,
    )

    data class KombinereUfoeretrygdMedInntekt(
        val ufoeregrad: Int,
        val utbetalingsgrad: Int,
        val inntektEtterUfoerhet: Kroner, // kan bli null
        val oppjustertInntektEtterUfoerhet: Kroner,
        val inntektsgrense: Kroner,
        val inntektsgrenseFaktisk: Kroner,
        val kompensasjonsgrad: Double,
        val forventetInntekt: Kroner,
        val nettoAkk_pluss_NettoRestAr: Kroner, // what is this?
        val nettoAkk: Kroner,
        val uforetrygdordinarNetto: Kroner,
        val uforetrygdberegningUforegrad: Double // (procent - Int or Double?)


    )

    data class BarnetilleggFellesbarn(
        val beloepBrutto: Kroner,

    )

    data class BarnetilleggSaerkullsbarn(
        val beloepBrutto: Kroner,

    )

}