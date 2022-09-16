package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import java.time.LocalDate

@Suppress("unused")
data class OpphoererBarnetilleggAutoDto(
    val foedselsdatoPaaBarnetilleggOpphoert: LocalDate,  //Vedtaksdata_Kravhode_KravlinjeListe_KravLinje_KravlinjeRelatertPerson?
    val oensketVirkningsDato: LocalDate,  //Vedtaksdata_Kravhode_onsketVirkningsDato
    val barnetilleggFellesbarn: BarnetilleggFellesbarn?,
    val barnetilleggSaerkullsbarn: BarnetilleggSaerkullsbarn?,
    val brukerBorInorge: Boolean,
    val grunnbeloep: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop
    val sivilstand: Sivilstand,  //Vedtaksdata_Kravhode_BeregningsData_BeregningUfore_BeregningSivilstandAnvendt
    val ufoeretrygd: Ufoeretrygd,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto,  //vedlegg
    val opplysningerBruktIBeregningUT: OpplysningerBruktIBeregningUTDto,  //vedlegg
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,  //vedlegg
)

data class Ufoeretrygd(
    val ufoertrygdUtbetalt: Int,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad
    val utbetaltPerMaaned: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto
    val ektefelletilleggUtbeltalt: Kroner?,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETnetto
    val gjenlevendetilleggUtbetalt: Kroner?,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTnetto
    val harUtbetalingsgrad: Boolean,

    )

data class BarnetilleggFellesbarn(
    val antallBarnInnvilget: Int,  //<Vedtaksbrev_Grunnlag_Persongrunnlagsliste_AntallBarn?
    val beloepFratrukketAnnenForeldersInntekt: Kroner,  //
    val beloepNettoFB: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto
    val fradragFB: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag
    val fribeloepFB: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop
    val inntektAnnenForelder: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBinntektAnnenForelder
    val inntektBruktIAvkortning: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBBrukersInntektTilAvkortning
    val inntektstak: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_Inntektstak
    val justeringsbeloepFB: Kroner
)

data class BarnetilleggSaerkullsbarn(
    val antallBarnInnvilget: Int,  //Grunnlag_Persongrunnlagsliste_AntallBarn?
    val beloepNettoSB: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto
    val fradragSB: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag
    val fribeloepSB: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop
    val inntektBruktIAvkortning: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning
    val inntektstak: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_Inntektstak
    val justeringsbeloepSB: Kroner
)
