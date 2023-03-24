package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import java.time.LocalDate

@Suppress("unused")
data class EndringIOpptjeningAutoDto(
    val endringIOpptjening: EndringIOpptjening,
    val endringIOpptjeningBeregning: EndringIOpptjeningBeregning?,
    val endringIOpptjeningBoolean: EndringIOpptjeningBoolean,
    val fellesbarnTillegg: FellesbarnTillegg?,
    val harEktefelletilleggInnvilget: Boolean?,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Ektefelletillegg
    val harGjenlevendetilleggInnvilget: Boolean?,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg
    val harYrkesskadeGradUtbetaling: Boolean?,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto,
    val opplysningerBruktIBeregningUT: OpplysningerBruktIBeregningUTDto,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
    val saerkullsbarnTillegg: SaerkullsbarnTillegg?,
    val sivilstand: Sivilstand,
    val virkningsDato: LocalDate,
) {


    data class EndringIOpptjening(
        val beloepsgrense: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense
        val forventetInntekt: Kroner,  //  Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt
        val grunnbeloep: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop
        val inntektsgrense: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense
        val inntektsgrenseNesteAar: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense
        val inntektstak: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak
        val kompensasjonsgrad: Double,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad
        val nettoAkkumulerteBeloepUtbetalt: Kroner,  // <NettoAkk> Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk
        val nettoTilUtbetalingRestenAvAaret: Kroner,  // Kan bli et negativt tall <NettoRestAr> edtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoRestAr
        val nettoUfoeretrygdUtbetaltPerMaaned: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Netto
        val oppjustertInntektEtterUfoere: Kroner,  // <Oieu> Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oieu
        val oppjustertInntektFoerUfoere: Kroner,  // <Oifu> Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu
        val ufoeregrad: Int, // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegningu_ufoeregrad
        val ufoertrygdUtbetalt: Kroner,
        val utbetalingsgrad: Int, // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_utbetalingsgrad
        val utbetaltPerMaaned: Kroner,
    )

    data class EndringIOpptjeningBeregning(
        val nettoAkkumulertePlussNettoRestAar: Kroner,  // <NettoAkk> + <NettoRestAr> Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoRestAr
        val oppjustertInntektFoerUfoere80prosent: Kroner,  // <Oifu * 0.8) Beregning i Exstream! Tror verdien finnes i Pesys
    )

    data class EndringIOpptjeningBoolean(
        val brukerBorInorge: Boolean,
        val harBeloepOekt: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt
        val harBeloepRedusert: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert
        val harDelvisUfoeregrad: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegningu_ufoeregrad > 0 < 100
        val harFullUfoeregrad: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegningu_ufoeregrad = 100
        val harFullUtbetalingsgrad: Boolean,
        val harInntektEtterUfoere: Boolean,  // IEUInntekt > 0 / Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense
        val harUfoeretrygdUtbetalt: Boolean,
        val harUtbetalingsgrad:Boolean,
        )

    data class FellesbarnTillegg(
        val beloepBrutto: Kroner,
        val beloepNetto: Kroner,
        val fribeloep: Kroner,
        val gjelderFlereBarn: Boolean,
        val harFellesbarnInnvilget: Boolean,
        val harFradrag: Boolean,
        val harFratrukketBeloepFraAnnenForelder: Boolean,
        val harJusteringsbeloep: Boolean,
        val inntektAnnenForelder: Kroner,
        val inntektBruktIAvkortning: Kroner,
        val inntektstak: Kroner,
        )

    data class SaerkullsbarnTillegg(
        val beloepBrutto: Kroner,
        val beloepNetto: Kroner,
        val fribeloep: Kroner,
        val gjelderFlereBarn: Boolean,
        val harFradrag: Boolean,
        val harJusteringsbeloep: Boolean,
        val harSaerkullsbarnInnvilget: Boolean,
        val inntektBruktIAvkortning: Kroner,
        val inntektstak: Kroner,
        )

}