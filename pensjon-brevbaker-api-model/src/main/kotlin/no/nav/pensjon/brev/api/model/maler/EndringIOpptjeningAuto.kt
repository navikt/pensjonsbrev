package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import java.time.LocalDate

@Suppress("unused")
data class EndringIOpptjeningAutoDto(
    val avkortningsinformasjon: Avkortningsinformasjon,
    val brukerBorInorge: Boolean,
    val fellesbarnTillegg: FellesbarnTillegg?,
    val grunnbeloep: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop,
    val harEktefelletilleggInnvilget: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Ektefelletillegg
    val harGjenlevendetilleggInnvilget: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg
    val harOektUtbetaling: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt  TODO: FJERNE? Nei, this is a boolean in Pesys, netto utbetaling av uføretrygd økes for resten av året.
    val harRedusertUtbetaling: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert  TODO: FJERNE? Nei, netto utbetaling av uføretrygd reduseres for resten av året.
    val harYrkesskadeOppfylt: Boolean,  // Uforetrygdberegning.Yrkesskadegrad
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto,
    val opplysningerBruktIBeregningUT: OpplysningerBruktIBeregningUTDto,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
    val saerkullsbarnTillegg: SaerkullsbarnTillegg?,
    val sivilstand: Sivilstand,
    val ufoeregrad: Int, // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegningu_ufoeregrad
    val ufoeretrygdOrdinaer: UfoeretrygdOrdinaer,
    val ufoertrygdUtbetalt: Kroner,
    val utbetaltPerMaaned: Kroner,
    val virkningsDato: LocalDate,
    ) {

    data class UfoeretrygdOrdinaer(
        val avkortningsinformasjon: Avkortningsinformasjon,
        val nettoAkkumulerteBeloepPlussNettoTilUtbetalingRestenAvAaret: Kroner,  // TODO: Beregnes i Exstream: <NettoAkk> + <NettoRestAr> Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoRestAr
        val nettoAkkumulerteBeloepUtbetalt: Kroner,  // <NettoAkk> Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk
        val nettoTilUtbetalingRestenAvAaret: Kroner,  // Kan bli et negativt tall <NettoRestAr> edtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoRestAr
        val nettoUfoeretrygdUtbetaltPerMaaned: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Netto

    )

    data class Avkortningsinformasjon(
        val beloepsgrense: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense
        val forventetInntekt: Kroner,  //  Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt
        val harInntektEtterUfoere: Boolean,  // IEU Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense
        val inntektsgrense: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense
        val inntektsgrenseNesteAar: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense
        val inntektstak: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak
        val kompensasjonsgrad: Double,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad
        val oppjustertInntektEtterUfoere: Kroner,  // <Oieu> Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oieu
        val oppjustertInntektFoerUfoere: Kroner,  // <Oifu> Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu
        val oppjustertInntektFoerUfoere80prosent: Kroner,  // TODO: Beregnes i Exstream: <Oifu * 0.8) : Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu
        val utbetalingsgrad: Int, // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_utbetalingsgrad

    )

    data class FellesbarnTillegg(
        // Fellesbarn1
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
        // Saerkullsbarn1
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