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
    val antallFellesbarnInnvilget: Int,  //<Vedtaksbrev_Grunnlag_Persongrunnlagsliste_AntallBarn?
    val beloepFratrukketAnnenForeldersInntekt: Kroner,  //
    val beloepNettoFellesbarn: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto
    val fradragFellesbarn: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag
    val fribeloepFellesbarn: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop
    val inntektAnnenForelderFellesbarn: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBinntektAnnenForelder
    val inntektBruktIAvkortningFellesbarn: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBBrukersInntektTilAvkortning
    val inntektstakFellesbarn: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_Inntektstak
    val justeringsbeloepFellesbarn: Kroner
)

data class BarnetilleggSaerkullsbarn(
    val antallSaerkullsbarnbarnInnvilget: Int,  //Grunnlag_Persongrunnlagsliste_AntallBarn?
    val beloepNettoSaerkullsbarn: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto
    val fradragSaerkullsbarn: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag
    val fribeloepSaerkullsbarn: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop
    val inntektBruktIAvkortningSaerkullsbarn: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning
    val inntektstakSaerkullsbarn: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_Inntektstak
    val justeringsbeloepSaerkullsbarn: Kroner
)
