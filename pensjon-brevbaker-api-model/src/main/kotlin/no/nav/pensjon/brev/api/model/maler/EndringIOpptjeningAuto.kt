package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import java.time.LocalDate

@Suppress("unused")
data class EndringIOpptjeningAutoDto(
    val brukerBorInorge: Boolean,
    val fellesbarn1: Fellesbarn1?,
    val grunnbeloep: Kroner,
    val harEktefelletilleggInnvilget: Boolean,
    val harGjenlevendetilleggInnvilget: Boolean,
    val harOektUtbetaling: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt  TODO: FJERNE? Nei, this is a boolean in Pesys, netto utbetaling av uføretrygd økes for resten av året.
    val harRedusertUtbetaling: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert  TODO: FJERNE? Nei, netto utbetaling av uføretrygd reduseres for resten av året.
    val harYrkesskadeOppfylt: Boolean,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto,
    val opplysningerBruktIBeregningUT: OpplysningerBruktIBeregningUTDto,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
    val saerkullsbarn1: Saerkullsbarn1?,
    val sivilstand: Sivilstand,
    val ufoeregrad: Int,
    val ufoeretrygdOrdinaer1: UfoeretrygdOrdinaer1,
    val ufoertrygdUtbetalt: Kroner,
    val utbetaltPerMaaned: Kroner,
    val virkningsDato: LocalDate,
    ) {

    data class UfoeretrygdOrdinaer1(
        val avkortningsinformasjon: Avkortningsinformasjon,
        val nettoAkkumulerteBeloepPlussNettoTilUtbetalingRestenAvAaret: Kroner,  // TODO: Beregnes i Exstream: <NettoAkk> + <NettoRestAr> Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoRestAr
        val nettoAkkumulerteBeloepUtbetalt: Kroner,  // <NettoAkk> Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk
        val nettoTilUtbetalingRestenAvAaret: Kroner,  // <NettoRestAr> Kan bli et negativt tall
        val nettoUfoeretrygdUtbetaltPerMaaned: Kroner,  // <Netto>

    ) {

    data class Avkortningsinformasjon(
        val beloepsgrense: Kroner,
        val forventetInntekt: Kroner,
        val harInntektEtterUfoere: Boolean,
        val inntektsgrense: Kroner,
        val inntektsgrenseNesteAar: Kroner,
        val inntektstak: Kroner,
        val kompensasjonsgrad: Double,
        val oppjustertInntektEtterUfoere: Kroner,  // <Oieu>
        val oppjustertInntektFoerUfoere: Kroner,  // <Oifu>
        val oppjustertInntektFoerUfoere80prosent: Kroner,  // TODO: Beregnes i Exstream: <Oifu * 0.8) : Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu
        val utbetalingsgrad: Int,

    ) }

    data class Fellesbarn1(
        val beloepBrutto: Kroner,
        val beloepNetto: Kroner,
        val fribeloep: Kroner,
        val gjelderFlereBarn: Boolean,
        val harFradrag: Boolean,
        val harFratrukketBeloepFraAnnenForelder: Boolean,
        val harJusteringsbeloep: Boolean,
        val inntektAnnenForelder: Kroner,
        val inntektBruktIAvkortning: Kroner,
        val inntektstak: Kroner,
    )

    data class Saerkullsbarn1(
        val beloepBrutto: Kroner,
        val beloepNetto: Kroner,
        val fribeloep: Kroner,
        val gjelderFlereBarn: Boolean,
        val harFradrag: Boolean,
        val harJusteringsbeloep: Boolean,
        val inntektBruktIAvkortning: Kroner,
        val inntektstak: Kroner,
    )

}