package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.*
import java.time.LocalDate

data class OpplysningerBruktIBeregningUTDto(
    val barnetilleggGjeldende: BarnetilleggGjeldende?,
    val beregnetUTPerManedGjeldende: BeregnetUTPerManedGjeldende,
    val borIUtlandet: Boolean,  // TODO: (PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) <> "nor" AND (PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland) <> ""
    val fraOgMedDatoErNesteAar: Boolean,
    val gjenlevendetilleggGjeldene: GjenlevendetilleggGjeldene?,
    val grunnbeloep: Kroner,
    val harBarnetilleggInnvilget: Boolean?,  // TODO: IF (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)
    val harEktefelletilleggInnvilget: Boolean?,  // TODO: PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
    val harKravaarsakEndringInntekt: Boolean,
    val inntektEtterUfoereGjeldende_beloepIEU: Kroner?,
    val inntektFoerUfoereGjeldende: InntektFoerUfoereGjeldende,
    val inntektFoerUfoereBegrunnelse: InntektFoerUfoereBegrunnelse,
    val inntektsAvkortingGjeldende: InntektsAvkortingGjeldende,
    val kravAarsakType: KravAarsakType,  // TODO: PE_Vedtaksdata_Kravhode_KravArsakType
    val minsteytelseGjeldende_sats: Double?,
    val norskTrygdetid: List<NorskTrygdetid>,
    val opptjeningAvdoedUfoeretrygd: OpptjeningUfoeretrygd?,
    val opptjeningUfoeretrygd: OpptjeningUfoeretrygd?,
    val sivilstand: Sivilstand,
    val trygdetidGjeldende: TrygdetidGjeldende,
    val trygdetidsdetaljerGjeldende: TrygdetidsdetaljerGjeldende,
    val ufoeretrygdGjeldende: UfoeretrygdGjeldende,
    val ungUfoerGjeldende_erUnder20Aar: Boolean?,
    val utenlandskTrygdetid: List<UtenlandskTrygdetid>,
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
        val totaltAntallBarn: Int,
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
        val kompensasjonsgrad: Double,
        val ufoeregrad: Int,
        val ufoeretidspunkt: LocalDate,
    )

    data class InntektsAvkortingGjeldende(
        val forventetInntektAar: Kroner,
        val inntektsgrenseAar: Kroner,
        val inntektstak: Kroner,
    )

    data class InntektFoerUfoereGjeldende(
        val erSannsynligEndret: Boolean,
        val ifuInntekt: Kroner,
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

    data class UtenlandskTrygdetid(
        val trygdetidLand: String,
        val trygdetidFom: LocalDate,
        val trygdetidTom: LocalDate
    )

    data class GjenlevendetilleggGjeldene(
        val harGjenlevendetillegg: Boolean,  // TODO: PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
        val harNyttGjenlevendetillegg: Boolean,  // TODO: PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_NyttGjenlevendetillegg = true
    )

}
