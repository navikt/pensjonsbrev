package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class EndringOpptjeningAutoDto(
    val barnetilleggFellesbarn: BarnetilleggFellesbarn?,
    val barnetilleggSaerkullsbarn: BarnetilleggSaerkullsbarn?,
    val endringIOpptjening: EndringIOpptjening,
    val kombinereUfoeretrygdMedInntekt: KombinereUfoeretrygdMedInntekt,
    val ufoeretrygd: Ufoeretrygd,  // used
) {

    data class EndringIOpptjening(
        val ufoerBeloepOekt: Boolean,  // used
        val ufoerBeloepRedusert: Boolean,  // used
        val virkningsDato: LocalDate,  // used
    )

    data class Ufoeretrygd(
        val ektefelletilleggInnvilget: Boolean,  // used
        val fellesbarnInnvilget: Boolean,  // used
        val gjenlevendetilleggInnvilget: Boolean,  // used
        val harUtbetalingsgrad: Boolean,  // used
        val harYrkesskadeGradUtbetaling: Boolean, // yrkesskadeGrad > 0  - used
        val saerkullsbarnInnvilget: Boolean,  // used
        val ufoertrygdUtbetalt: Kroner,
        val utbetaltPerMaaned: Kroner,  // used
    )

    data class KombinereUfoeretrygdMedInntekt(
        val ufoeregrad: Int, // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegningu_ufoeregrad
        val utbetalingsgrad: Int, // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_utbetalingsgrad
        val inntektsgrenseNesteAar: Kroner,  //
        val inntektsgrense: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_InntektsgrenseNesteAr
        val harInntektEtterUfoere: Boolean,  // IEUInntekt > 0 / Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense
        val beloepsgrense: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense
        val grunnbeloep: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop
        val oieu: Kroner,  // ? Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oieu
        val harBeloepsgrense60000: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense = 60000
        val harFullUfoeregrad: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegningu_ufoeregrad = 100
        val harDelvisUfoeregrad: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegningu_ufoeregrad > 0 < 100
        val inntektstak: Kroner,  // OIFU*0,8  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak ??
        val kompensasjonsgrad: Int,  //
        val forventetInntekt: Kroner  //

        // oifu = OppjustertInntektFoerUfoere
        // oieu = OppjustertInntektEtterUfoere
    )

    data class BarnetilleggFellesbarn(
        val beloepBrutto: Kroner,

        )

    data class BarnetilleggSaerkullsbarn(
        val beloepBrutto: Kroner,

        )

}