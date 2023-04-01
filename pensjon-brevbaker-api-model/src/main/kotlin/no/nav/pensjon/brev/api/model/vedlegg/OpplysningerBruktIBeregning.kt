package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.*
import java.time.LocalDate

data class OpplysningerBruktIBeregningUTDto(
    val barnetilleggGjeldende: BarnetilleggGjeldende?,
    val beregnetUTPerManedGjeldende: BeregnetUTPerManedGjeldende,
    val beregningUfoere: BeregningUfoere,
    val borIUtlandet: Boolean,  // PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland <> "nor" AND PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland <> ""
    val fraOgMedDatoErNesteAar: Boolean,
    val gjenlevendetilleggTabell: GjenlevendetilleggTabell?,
    val harBarnetilleggInnvilget: Boolean?,  // (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)
    val harBrukerKonvertertUP: Boolean,
    val harEktefelletilleggInnvilget: Boolean?,  // TODO: PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
    val harGodkjentBrevkode: Boolean,  // TODO  **
    val harKravaarsakEndringInntekt: Boolean,
    val inntektEtterUfoereGjeldende_beloepIEU: Kroner?,
    val inntektFoerUfoereBegrunnelse: InntektFoerUfoereBegrunnelse,
    val inntektFoerUfoereGjeldende: InntektFoerUfoereGjeldende,
    val inntektsAvkortingGjeldende: InntektsAvkortingGjeldende,
    val kravAarsakType: KravAarsakType,  // TODO: PE_Vedtaksdata_Kravhode_KravArsakType
    val minsteytelseGjeldende_sats: Double?,
    val norskTrygdetid: List<NorskTrygdetid>, // TODO
    val opptjeningAvdoedUfoeretrygd: OpptjeningUfoeretrygd?, // TODO
    val opptjeningUfoeretrygd: OpptjeningUfoeretrygd?, // TODO
    val sivilstand: Sivilstand,
    val trygdetidGjeldende: TrygdetidGjeldende,
    val trygdetidsdetaljerGjeldende: TrygdetidsdetaljerGjeldende,
    val ufoeretrygdGjeldende: UfoeretrygdGjeldende,
    val ufoeretrygdOrdinaer: UfoeretrygdOrdinaer, // TODO
    val ungUfoerGjeldende_erUnder20Aar: Boolean?,
    val utenlandskTrygdetidBilateral: List<UtenlandskTrygdetidBilateral>, // TODO
    val utenlandskTrygdetidEOS: List<UtenlandskTrygdetidEOS>, // TODO
    val yrkesskadeGjeldende: YrkesskadeGjeldende?,

) {

    data class YrkesskadeGjeldende(
        val beregningsgrunnlagBeloepAar: Kroner,
        val inntektVedSkadetidspunkt: Kroner,
        val skadetidspunkt: LocalDate,
        val yrkesskadegrad: Int,
    )

    data class BarnetilleggGjeldende(
        val saerkullsbarn: Saerkullsbarn?,
        val fellesbarn: Fellesbarn?,
        val totaltAntallBarn: Int, //TODO remove in next version. The following list can be used
        val foedselsdatoPaaBarnTilleggetGjelder: List<LocalDate>,
    ) {
        data class Saerkullsbarn(
            val avkortningsbeloepAar: Kroner,
            val beloepNetto: Kroner,
            val beloepBrutto: Kroner,
            val beloepAarNetto: Kroner,
            val beloepAarBrutto: Kroner,
            val erRedusertMotinntekt: Boolean,
            val fribeloep: Kroner,
            val fribeloepEllerInntektErPeriodisert: Boolean,
            val harFlereBarn: Boolean,
            val inntektBruktIAvkortning: Kroner,
            val inntektOverFribeloep: Kroner,
            val inntektstak: Kroner,
            val justeringsbeloepAar: Kroner,
        )

        data class Fellesbarn(
            val avkortningsbeloepAar: Kroner,
            val beloepNetto: Kroner,
            val beloepBrutto: Kroner,
            val beloepAarNetto: Kroner,
            val beloepAarBrutto: Kroner,
            val beloepFratrukketAnnenForeldersInntekt: Kroner,
            val erRedusertMotinntekt: Boolean,
            val fribeloep: Kroner,
            val fribeloepEllerInntektErPeriodisert: Boolean,
            val harFlereBarn: Boolean,
            val inntektAnnenForelder: Kroner,
            val inntektBruktIAvkortning: Kroner,
            val inntektOverFribeloep: Kroner,
            val inntektstak: Kroner,
            val justeringsbeloepAar: Kroner,
        )
    }

    data class TrygdetidsdetaljerGjeldende(
        val anvendtTT: Int,
        val beregningsmetode: Beregningsmetode,
        val faktiskTTEOS: Int?,
        val faktiskTTNordiskKonv: Int?,
        val faktiskTTNorge: Int?,
        val framtidigTTNorsk: Int?,
        val nevnerTTEOS: Int?,
        val nevnerTTNordiskKonv: Int?,
        val samletTTNordiskKonv: Int?,
        val tellerTTEOS: Int?,
        val tellerTTNordiskKonv: Int?,
        val utenforEOSogNorden: UtenforEOSogNorden?,
    ) {

        data class UtenforEOSogNorden(
            val faktiskTTBilateral: Int,
            val tellerProRata: Int,
            val nevnerProRata: Int,
        )
    }

    data class BeregnetUTPerManedGjeldende(
        val brukerErFlyktning: Boolean,
        val brukersSivilstand: Sivilstand,
        val grunnbeloep: Kroner,
        val virkDatoFom: LocalDate,
    )

    data class UfoeretrygdGjeldende(
        val beloepsgrense: Kroner,
        val beregningsgrunnlagBeloepAar: Kroner,
        val erKonvertert: Boolean,
        val harDelvisUfoeregrad: Boolean,  // TODO: Ny
        val harFullUfoeregrad: Boolean, // TODO: Ny
        val harUtbetalingsgradLessThanUfoeregrad: Boolean,
        val kompensasjonsgrad: Double,
        val ufoeregrad: Int,
        val ufoeretidspunkt: LocalDate,
        val ugradertBruttoPerAar: Kroner, // TODO: Ny
    )

    data class InntektsAvkortingGjeldende(
        val forventetInntektAar: Kroner,
        val harForventetInntektLargerThanInntektstak: Boolean,
        val harInntektsgrenseLargerThanOrEqualToInntektstak: Boolean,
        val inntektsgrenseAar: Kroner,
        val inntektstak: Kroner,
    )

    data class InntektFoerUfoereGjeldende(
        val erSannsynligEndret: Boolean,
        val ifuInntekt: Kroner,
        val oifuInntekt: Kroner, // TODO
    )

    // TODO: Create in Pesys
    data class Opptjeningsperiode(
        val aar: Year,
        val erBrukt: Boolean,
        val harBeregningsmetodeFolketrygd: Boolean,
        val harFoerstegangstjenesteOpptjening: Boolean,
        val harInntektAvtaleland: Boolean,
        val harOmsorgsopptjening: Boolean,
        val inntektAvkortet: Kroner,
        val justertPensjonsgivendeInntekt: Kroner,
        val pensjonsgivendeInntekt: Kroner,
    )

    data class OpptjeningUfoeretrygd(
        val harFoerstegangstjenesteOpptjening: Boolean,
        val harOmsorgsopptjening: Boolean,
        val opptjeningsperioder: List<Opptjeningsperiode>,
    )

    data class TrygdetidGjeldende(
        val fastsattTrygdetid: Int,
        val har40AarFastsattTrygdetid: Boolean,
        val harFramtidigTrygdetidEOS: Boolean,
        val harFramtidigTrygdetidNorsk: Boolean,
        val harLikUfoeregradOgYrkesskadegrad: Boolean,
        val harTrygdetidsgrunnlag: Boolean,
        val harYrkesskadeOppfylt: Boolean,
    )

    data class NorskTrygdetid(
        val trygdetidFom: LocalDate,
        val trygdetidTom: LocalDate
    )

    data class UtenlandskTrygdetidEOS(
        val trygdetidEOSLand: String,
        val trygdetidFom: LocalDate,
        val trygdetidTom: LocalDate,
    )

    data class UtenlandskTrygdetidBilateral(
        val trygdetidBilateralLand: String,
        val trygdetidFom: LocalDate,
        val trygdetidTom: LocalDate,
    )


    data class UfoeretrygdOrdinaer(
        val fradrag: Kroner,
        val harBeloepRedusert: Boolean,  // PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert
        val harNyUTBeloep: Boolean, // TODO: Ny / PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT
        val harTotalNettoUT: Boolean,  // PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto
        val nettoAkkumulerteBeloepUtbetalt: Kroner,  // NettoAkk
        val nettoTilUtbetalingRestenAvAaret: Kroner,  // NettoRestAr
    )

    data class BeregningUfoere(
        val harGammelUTBeloepUlikNyUTBeloep: Boolean, // TODO: PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT
        val harInntektsgrenseLessThanInntektstak: Boolean,  // TODO: PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak
        val nettoAkkumulertePlussNettoRestAar: Kroner,  // TODO: NettoAkk + NettoRestAr
        val nettoPerAarReduksjonUT: Kroner, // TODO: overskytenedeInntekt X kompensasjonsgrad
        val overskytendeInntekt: Kroner,  // TODO: PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt - PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense
        val ufoeretrygdPlussInntekt: Kroner,  // TODO: NettoAkk + NettoRestAr + ForventetInntekt
    )

    data class GjenlevendetilleggTabell(
        val anvendtTTAvdoed: Int,  // PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_AnvendtTrygdetid
        val beregningsgrunnlagBeloepAarAvdoed: Kroner,  // PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdod
        val beregningsgrunnlagBeloepAarYrkesskadeAvdoed: Kroner?, // PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodYrkesskadeArsbelop
        val erFlyktningAvdoed: Boolean,  // PE_Grunnlag_PersongrunnlagAvdod_BrukerFlyktning
        val erUngUfoerAvdoed: Boolean, // PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_MinsteYtelseBenyttetUngUfor
        val faktiskTTBilateralAvdoed: Int?,  // PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_FaTTBilateral
        val faktiskTTEOSAvdoed: Int?,
        val faktiskTTNordiskKonvAvdoed: Int?,
        val faktiskTTNorgeAvdoed: Int?,  // PE_Vedtaksdata_TrygdetidAvdod_FaTTNorge
        val faktiskTrygdetidINorgePlusAvtalelandAvdoed: Int?, // TODO: Summeres i Extream: PE_Vedtaksdata_TrygdetidAvdod_FaTTNorge + PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_FaTTBilateral
        val foedselsnummerAvdoed: Foedselsnummer,
        val framtidigTTAvtalelandAvdoed: Int?,  //
        val framtidigTTEOSAvdoed: Int?,  // PE_Vedtaksdata_TrygdetidAvdod_FramtidigTTEOS
        val framtidigTTNorskAvdoed: Int?, // PE_Vedtaksdata_TrygdetidAvdod_FramtidigTTNorsk
        val harNyttGjenlevendetillegg: Boolean,  // PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_NyttGjenlevendetillegg
        val inntektVedSkadetidspunktAvdoed: Kroner?, // PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_InntektVedSkadetidspunkt
        val nevnerTTBilateralProRataAvdoed: Int?,
        val nevnerTTEOSAvdoed: Int?,  // PE_Vedtaksdata_TrygdetidAvdod_TTNevnerEOS
        val nevnerTTNordiskKonvAvdoed: Int?,  // PE_Vedtaksdata_TrygdetidAvdod_TTNevnerNordisk
        val samletTTNordiskKonvAvdoed: Int?,  // TODO: Summeres i Exstream:  FaTTNorge + FaTT_A10netto
        val samletTrygdetidEtterAvkortingAvdoed: Int?,  // TODO: Summeres i Exstream: PE_Vedtaksdata_TrygdetidAvdod_FaTTNorge +
        val tellerTTBilateralProRataAvdoed: Int?,
        val tellerTTEOSAvdoed: Int?,  // PE_Vedtaksdata_TrygdetidAvdod_TTTellerEOS
        val tellerTTNordiskKonvAvdoed: Int?,  // PE_Vedtaksdata_TrygdetidAvdod_TTTellerNordisk
        val ufoeretidspunktAvdoed: LocalDate?,  // PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_Uforetidspunkt
        val yrkesskadegradAvdoed: Int?,  // PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_Yrkesskadegrad
    )
}
