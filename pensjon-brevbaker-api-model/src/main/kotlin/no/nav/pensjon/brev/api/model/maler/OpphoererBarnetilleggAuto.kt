package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import java.time.LocalDate

@Suppress("unused")
data class OpphoererBarnetilleggAutoDto(
    val btFribeloep: BTFribeloep,
    val btHjemmelInnvilget: BTHjemmelInnvilget,
    val btInntektstak: BTInntektstak,
    val ektefelletilleggInnvilget: Boolean,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget
    val ektefelletilleggUtbeltalt: Kroner?,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETnetto
    val fellesbarn: Fellesbarn?,
    val foedselsdatoPaaBarnetilleggOpphoert: LocalDate,  //Vedtaksdata_Kravhode_KravlinjeListe_KravLinje_KravlinjeRelatertPerson?
    val gjenlevendetilleggInnvilget: Boolean,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget
    val gjenlevendetilleggUtbetalt: Kroner?,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTnetto
    val grunnbeloep: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop
    val maanedligUfoeretrygdFoerSkattDto: MaanedligUfoeretrygdFoerSkattDto,  //vedlegg
    val btOensketVirkningsDato: BTOensketVirkningsDato,
    val opplysningerBruktIBeregningUTDto: OpplysningerBruktIBeregningUTDto,  //vedlegg
    val orienteringOmRettigheterUfoereDto: OrienteringOmRettigheterUfoereDto,  //vedlegg
    val saerkullsbarn: Saerkullsbarn?,
    val sivilstand: Sivilstand,  //Vedtaksdata_Kravhode_BeregningsData_BeregningUfore_BeregningSivilstandAnvendt  Er mapping fra Pesys riktig?
    val utbetaltPerMaaned: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto
    val ufoertrygdUtbetalt: Int,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad
)

data class Fellesbarn(
    val antallBarnInnvilget: Int,  //<Vedtaksbrev_Grunnlag_Persongrunnlagsliste_AntallBarn?
    val barnetilleggInnvilget: Boolean,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget
    val barnetilleggUtbetalt: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto
    val fradrag: Int,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag
    val fribeloep: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop
    val inntektAnnenForelder: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBinntektAnnenForelder
    val inntektBruktIAvkortning: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBBrukersInntektTilAvkortning
    val inntektstak: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_Inntektstak
)

data class Saerkullsbarn(
    val antallBarnInnvilget: Int,  //Grunnlag_Persongrunnlagsliste_AntallBarn?
    val barnetilleggInnvilget: Boolean,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget
    val barnetilleggUtbetalt: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto
    val fribeloep: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop
    val inntektBruktIAvkortning: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning
    val inntektstak: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_Inntektstak
)

data class BTHjemmelInnvilget(
    val fellesbarnInnvilget: Boolean,
    val saerkullsbarnInnvilget: Boolean
)

data class BTFribeloep(
    val fellesbarnFribeloep: Kroner,  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop
    val saerkullsbarnFribeloep: Kroner  //Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop
)

data class BTOensketVirkningsDato(
    val oensketVirkningsDato: LocalDate  //Vedtaksdata_Kravhode_onsketVirkningsDato
)

data class BTInntektstak(
    val fellesbarnInntektstak: Kroner,  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_Inntektstak
    val saerkullsbarnBTInntektstak: Kroner  //Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_Inntektstak
)