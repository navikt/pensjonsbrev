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
        val harGjenlevendetilleggInnvilget: Boolean,  // used
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
        val oppjustertInntektEtterUfoere: Kroner,  // <Oieu> Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oieu
        val harFullUfoeregrad: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegningu_ufoeregrad = 100
        val harDelvisUfoeregrad: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegningu_ufoeregrad > 0 < 100
        val kompensasjonsgrad: Int,  //
        val forventetInntekt: Kroner,  //
        val oppjustertInntektFoerUfoere: Kroner,  // <Oifu>
        val oppjustertInntektFoerUfoere80prosent: Kroner,  // TODO: <Oifu * 0.8) Beregning i Exstream! Tror verdien finnes i Pesys
        val nettoAkkumulerteBeloepUtbetalt: Kroner,  // <NettoAkk> Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk
        val nettoTilUtbetalingRestenAvAaret: Kroner,  // Kan bli et negativt tall <NettoRestAr>
        val nettoUfoeretrygdUtbetaltPerMaaned: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Netto
        val harBeloepRedusert: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert
        val harBeloepOekt: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt
        val nettoAkkumulertePlussNettoRestAar: Kroner  // TODO: <NettoAkk_pluss_NettoRestAr> Beregning i Exstream!  PE_UT_NettoAkk_pluss_NettoRestAr. Tror ikke verdien finnes i Pesys


    )

    data class BarnetilleggFellesbarn(
        val beloepBrutto: Kroner,

        )

    data class BarnetilleggSaerkullsbarn(
        val beloepBrutto: Kroner,

        )

}