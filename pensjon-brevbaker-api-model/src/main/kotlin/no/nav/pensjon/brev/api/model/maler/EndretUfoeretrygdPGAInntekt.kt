package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year
import java.time.LocalDate

@Suppress("unused")
data class EndretUfoeretrygdPGAInntektDto(
    val PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus: Kroner,
    val PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus: Kroner,
    val PE_UT_NettoAkk_pluss_NettoRestAr: Kroner,
    val PE_UT_VirkningstidpunktArMinus1Ar: Int,
    val PE_VedtaksData_VirkningFOM: LocalDate,

    val harTilleggForFlereBarn: Boolean,
    val barnetilleggReduksjonsgrunnlagHarRegelverkstypeOvergang2016: Boolean,

    val barnetilleggFelles_InntektBruktIAvkortning: Kroner,
    val barnetilleggFelles_avkortningsbeloepPerAar: Kroner,
    val barnetilleggFelles_belopFratrukketAnnenForeldersInntekt: Kroner,
    val barnetilleggFelles_brukersInntektTilAvkortning: Kroner,
    val barnetilleggFelles_fribeloep: Kroner,
    val barnetilleggFelles_fribelopPeriodisert: Boolean,
    val barnetilleggFelles_inntektAnnenForelder: Kroner,
    val barnetilleggFelles_inntektPeriodisert: Boolean,
    val barnetilleggFelles_inntektstak: Kroner,
    val barnetilleggFelles_innvilget: Boolean,
    val barnetilleggFelles_justeringsbelopPerAr: Kroner,
    val barnetilleggFelles_netto: Kroner,
    val beregningUfore_BelopGammelBTFB: Kroner,
    val beregningUfore_BelopNyBTFB: Kroner,
    val harFlereFellesBarn: Boolean,

    val barnetilleggSerkull_avkortingsbelopPerAr: Kroner,
    val barnetilleggSerkull_fribeloep: Kroner,
    val barnetilleggSerkull_fribelopPeriodisert: Boolean,
    val barnetilleggSerkull_inntektBruktIAvkortning: Kroner,
    val barnetilleggSerkull_inntektPeriodisert: Boolean,
    val barnetilleggSerkull_inntektstak: Kroner,
    val barnetilleggSerkull_innvilget: Boolean,
    val barnetilleggSerkull_justeringsbelopPerAr: Kroner,
    val barnetilleggSerkull_netto: Kroner,
    val beregningUfore_BelopGammelBTSB: Kroner,
    val beregningUfore_BelopNyBTSB: Kroner,
    val harFlereSaerkullsbarn: Boolean,

    val beregningUfore_BelopGammelUT: Kroner,
    val beregningUfore_BelopNyUT: Kroner,
    val beregningUfore_BelopOkt: Boolean,
    val beregningUfore_BelopRedusert: Boolean,
    val beregningUfore_grunnbelop: Kroner,
    val beregningUfore_totalNetto: Kroner,
    val beregningUfore_uforegrad: Int,
    val beregningVirkFomErFoersteJanuar: Boolean,
    val borMedSivilstand: BorMedSivilstand,
    val brukerBorINorge: Boolean,
    val ektefelletillegg_ETinnvilget: Boolean,
    val fyller67iAar: Boolean,
    val gjenlevendetillegg_GTinnvilget: Boolean,
    val harInstitusjonsopphold: Boolean,
    val harKravaarsakSivilstandsendring: Boolean,
    val uforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad: Int,
    val uforetrygdOrdiner_avkortingsbelopPerAr: Kroner,
    val uforetrygdOrdiner_forventetInntekt: Kroner,
    val uforetrygdOrdiner_inntektsgrense: Kroner,
    val uforetrygdOrdiner_inntektstak: Kroner,
    val uforetrygdOrdiner_netto: Kroner,
    val uforetrygdOrdiner_nettoAkk: Kroner,
    val virkFomAar: Year,
    val virkFomAarMinusEn: Year,
    val virkFomErFoersteJanuar: Boolean,
): BrevbakerBrevdata
