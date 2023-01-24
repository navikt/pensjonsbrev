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
        val forventetInntekt: Kroner,  //  Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt
        val harEktefelletilleggInnvilget: Boolean,  // used
        val harFellesbarnInnvilget: Boolean,  // used
        val harGjenlevendetilleggInnvilget: Boolean,  // used
        val harSaerkullsbarnInnvilget: Boolean,  // used
        val harUtbetalingsgrad: Boolean,  // used
        val harYrkesskadeGradUtbetaling: Boolean, // yrkesskadeGrad > 0  - used
        val inntektsgrense: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense
        val ufoertrygdUtbetalt: Kroner,
        val utbetaltPerMaaned: Kroner,  // used
    )



    data class KombinereUfoeretrygdMedInntekt(
        val beloepsgrense: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense
        val forventetInntekt: Kroner,  //  Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt
        val grunnbeloep: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop
        val harBeloepOekt: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt
        val harBeloepRedusert: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert
        val harDelvisUfoeregrad: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegningu_ufoeregrad > 0 < 100
        val harFullUfoeregrad: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegningu_ufoeregrad = 100
        val harInntektEtterUfoere: Boolean,  // IEUInntekt > 0 / Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense
        val inntektsgrense: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense
        val inntektsgrenseNesteAar: Kroner,  //
        val inntektstak: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak
        val kompensasjonsgrad: Int,  //
        val nettoAkkumulerteBeloepUtbetalt: Kroner,  // <NettoAkk> Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk
        val nettoAkkumulertePlussNettoRestAar: Kroner,  // TODO: <NettoAkk_pluss_NettoRestAr> Beregning i Exstream!  PE_UT_NettoAkk_pluss_NettoRestAr. Tror ikke verdien finnes i Pesys
        val nettoTilUtbetalingRestenAvAaret: Kroner,  // Kan bli et negativt tall <NettoRestAr>
        val nettoUfoeretrygdUtbetaltPerMaaned: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Netto
        val oppjustertInntektEtterUfoere: Kroner,  // <Oieu> Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oieu
        val oppjustertInntektFoerUfoere80prosent: Kroner,  // TODO: <Oifu * 0.8) Beregning i Exstream! Tror verdien finnes i Pesys
        val oppjustertInntektFoerUfoere: Kroner,  // <Oifu>
        val ufoeregrad: Int, // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegningu_ufoeregrad
        val utbetalingsgrad: Int, // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_utbetalingsgrad


    )

    data class BarnetilleggFellesbarn(
        val beloepBrutto: Kroner,

        )

    data class BarnetilleggSaerkullsbarn(
        val beloepBrutto: Kroner,

        )

}