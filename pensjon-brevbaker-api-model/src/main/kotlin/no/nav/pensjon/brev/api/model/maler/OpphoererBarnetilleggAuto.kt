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
    val sivilstand: Sivilstand,  //Vedtaksdata_Kravhode_BeregningsData_BeregningUfore_BeregningSivilstandAnvendt  Er mapping fra Pesys riktig?
    val ufoeretrygd: Ufoeretrygd,
    val maanedligUfoeretrygdFoerSkattDto: MaanedligUfoeretrygdFoerSkattDto,  //vedlegg
    val opplysningerBruktIBeregningUTDto: OpplysningerBruktIBeregningUTDto,  //vedlegg
    val orienteringOmRettigheterUfoereDto: OrienteringOmRettigheterUfoereDto,  //vedlegg
)

data class Ufoeretrygd(
    val ufoertrygdUtbetalt: Int,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad
    val utbetaltPerMaaned: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto
    val ektefelletilleggUtbeltalt: Kroner?,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETnetto
    val gjenlevendetilleggUtbetalt: Kroner?,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTnetto
    val utbetalingsgrad: Int,

)
data class BarnetilleggFellesbarn(
    val antallBarnInnvilget: Int,  //<Vedtaksbrev_Grunnlag_Persongrunnlagsliste_AntallBarn?
    val beloepNetto: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto
    val fradrag: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag
    val fribeloep: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop
    val inntektAnnenForelder: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBinntektAnnenForelder
    val inntektBruktIAvkortning: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBBrukersInntektTilAvkortning
    val inntektstak: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_Inntektstak
)

data class BarnetilleggSaerkullsbarn(
    val antallBarnInnvilget: Int,  //Grunnlag_Persongrunnlagsliste_AntallBarn?
    val beloepNetto: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto
    val fribeloep: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop
    val inntektBruktIAvkortning: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning
    val inntektstak: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_Inntektstak
)
