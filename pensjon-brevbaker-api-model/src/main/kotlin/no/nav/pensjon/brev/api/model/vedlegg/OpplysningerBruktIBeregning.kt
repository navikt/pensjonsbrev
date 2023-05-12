package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year
import java.time.LocalDate

data class OpplysningerBruktIBeregningUTDto(
    val avdoed: OpplysningerBruktIBeregningUTAvdoed?,
    val barnetillegg: Barnetillegg?,
    val beregnetUTPerManed: BeregnetUTPerManed,
    val borIUtlandet: Boolean,  // PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland <> "nor" AND PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland <> ""
    val fraOgMedDatoErNesteAar: Boolean,
    val harEktefelletilleggInnvilget: Boolean,
    val harKravaarsakEndringInntekt: Boolean,
    val inntektEtterUfoereBeloepIEU: Kroner?,
    val inntektFoerUfoere: InntektFoerUfoere,
    val inntektsAvkorting: InntektsAvkorting,
    val kravAarsakType: KravAarsakType,
    val minsteytelseSats: Double?,  // Bruk denne istedenfor harMinsteytelse boolean. Null betyr at man ikke har minsteytelse
    val sivilstand: Sivilstand,
    val trygdetid: Trygdetid,
    val ufoeretrygd: Ufoeretrygd,
    val ungUfoerErUnder20Aar: Boolean?,  // Null betyr at man ikke har ungUfoer
    val yrkesskade: Yrkesskade?,
) {

    data class Yrkesskade(
        val beregningsgrunnlagBeloepAar: Kroner,
        val inntektVedSkadetidspunkt: Kroner,
        val skadetidspunkt: LocalDate,
        val yrkesskadegrad: Int,
    )

    data class Barnetillegg(
        val saerkullsbarn: Saerkullsbarn?,
        val fellesbarn: Fellesbarn?,
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
            val inntektBruktIAvkortning: Kroner, // TODO fjern i neste versjon
            val samletInntektBruktIAvkortning: Kroner,
            val inntektOverFribeloep: Kroner,
            val inntektstak: Kroner,
            val justeringsbeloepAar: Kroner,
        )
    }

    data class Trygdetid(
        val bilateralTrygdePerioder: List<UtenlandsTrygdetidPeriode>,
        val eosTrygdePerioder: List<UtenlandsTrygdetidPeriode>,
        val norskTrygdetidPerioder: List<NorskTrygdetidPeriode>,
        val trygdetidsdetaljer: Trygdetidsdetaljer,
        ) {
        data class Trygdetidsdetaljer(
            val anvendtTT: Int,
            val beregningsmetode: Beregningsmetode,
            val faktiskTTEOS: Int?,
            val faktiskTTNordiskKonv: Int?,
            val faktiskTTNorge: Int?,
            val fastsattNorskTrygdetid: Int?,  //  TODO: Beregnes i Exstream: (<FaTTNorge> + <FramtidigTTNorge>)/12
            val framtidigTTEOS: Int?,
            val framtidigTTNorsk: Int?,
            val harBoddArbeidUtland: Boolean,
            val nevnerTTEOS: Int?,
            val nevnerTTNordiskKonv: Int?,
            val samletTTNordiskKonv: Int?,
            val tellerTTEOS: Int?,
            val tellerTTNordiskKonv: Int?,
            val utenforEOSogNorden: UtenforEOSogNorden?,
        ) {
            data class UtenforEOSogNorden(
                val faktiskTTBilateral: Int,
                val nevnerProRata: Int,
                val tellerProRata: Int,
            )
        }
    }

    data class BeregnetUTPerManed(
        val brukerErFlyktning: Boolean,
        val brukersSivilstand: Sivilstand,
        val grunnbeloep: Kroner,
        val virkDatoFom: LocalDate,
    )

    data class Ufoeretrygd(
        val beloepsgrense: Kroner,
        val beregningsgrunnlagBeloepAar: Kroner,
        val erKonvertert: Boolean,
        val fullUfoeretrygdPerAar: Kroner,  // ugradertBruttoPerAar
        val harInntektEtterUfoereBegrunnelse: Boolean,
        val harUtbetalingsgradLessThanUfoeregrad: Boolean,
        val kompensasjonsgrad: Double,
        val ufoeregrad: Int,
        val ufoeretidspunkt: LocalDate,
        val ufoeretrygdOrdinaer: UfoeretrygdOrdinaer,
        )

    data class InntektsAvkorting(
        val forventetInntektAar: Kroner,
        val inntektsgrenseAar: Kroner,
        val inntektstak: Kroner,
    )

    data class InntektFoerUfoere(
        val erSannsynligEndret: Boolean,
        val inntektFoerUfoer: Kroner,
        val inntektFoerUfoereBegrunnelse: InntektFoerUfoereBegrunnelse,
        val oppjustertInntektFoerUfoer: Kroner,
        val opptjeningUfoeretrygd: OpptjeningUfoeretrygd?,
        )

    data class OpptjeningUfoeretrygd(
        val harFoerstegangstjenesteOpptjening: Boolean,
        val harOmsorgsopptjening: Boolean,
        val opptjeningsperiode: List<Opptjeningsperiode>,
    )

    data class Opptjeningsperiode(
        val aar: Year,
        val harFoerstegangstjenesteOpptjening: Boolean,
        val harInntektAvtaleland: Boolean,
        val harOmsorgsopptjening: Boolean,
        val justertPensjonsgivendeInntekt: Kroner,
        val pensjonsgivendeInntekt: Kroner,
    )

    data class NorskTrygdetidPeriode(
        val trygdetidFom: LocalDate,
        val trygdetidTom: LocalDate,
    )

    data class UtenlandsTrygdetidPeriode(
        val trygdetidBilateralLand: String,
        val trygdetidEOSLand: String,
        val trygdetidFom: LocalDate,
        val trygdetidTom: LocalDate,
    )

    data class UfoeretrygdOrdinaer(
        val harBeloepRedusert: Boolean,  // PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert
        val harNyUTBeloep: Boolean, // PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT
        val harTotalNettoUT: Boolean,  // PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto
        val nettoAkkumulerteBeloepUtbetalt: Kroner?,  // NettoAkk
        val nettoTilUtbetalingRestenAvAaret: Kroner?,  // NettoRestAr
        val reduksjonIUfoeretrygd: Kroner?,  // fradrag
        val harGammelUTBeloepUlikNyUTBeloep: Boolean, // TODO: Regnes ut i Exstream: PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT
        val nettoAkkumulertePlussNettoRestAar: Kroner?,  // TODO: Summeres i Exstream: NettoAkk + NettoRestAr
        val nettoPerAarReduksjonUT: Kroner?, // TODO: Summeres i Exstream: overskytenedeInntekt X kompensasjonsgrad
        val overskytendeInntekt: Kroner?,  // TODO: Summeres i Exstream: PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt - PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense
        val ufoeretrygdPlussInntekt: Kroner?,  // TODO: Summeres i Exstream: NettoAkk + NettoRestAr + ForventetInntekt
    )

    data class OpplysningerBruktIBeregningUTAvdoed(
        val erFlyktning: Boolean,
        val erUngUfoer: Boolean,
        val foedselsnummer: Foedselsnummer,
        val harNyttGjenlevendetillegg: Boolean,
        val norskTrygdetidPerioder: List<NorskTrygdetidPeriode>,
        val opptjeningUfoeretrygd: OpptjeningUfoeretrygd,
        val opptjeningsperioder: List<Opptjeningsperiode>,
        val trygdetidsdetaljer: TrygdetidsdetaljerAvdoed,
        val ufoeretrygd: Ufoeretrygd1,
        val bilateralTrygdePerioder: List<UtenlandsTrygdetidPeriode>,
        val eosTrygdePerioder: List<UtenlandsTrygdetidPeriode>,
        val yrkesskade: Yrkesskade1?,
    ) {
        data class TrygdetidsdetaljerAvdoed(
            val anvendtTT: Int,
            val beregningsmetode: Beregningsmetode,
            val faktiskTTBilateral: Int?,
            val faktiskTTEOS: Int?,
            val faktiskTTNordiskKonv: Int?,
            val faktiskTTNorge: Int?,
            val faktiskTTNorgePlusFaktiskBilateral: Int?, // TODO: Summeres i Extream: PE_Vedtaksdata_TrygdetidAvdod_FaTTNorge + PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_FaTTBilateral
            val faktiskTTNorgePlusfaktiskTTEOS: Int?,  // TODO: Summeres i Exstream: PE_Vedtaksdata_TrygdetidAvdod_FaTTNorge + PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_FaTTEOS
            val fastsattNorskTrygdetid: Int?,  // TODO: Summeres i Exstream
            val framtidigTTAvtaleland: Int?,
            val framtidigTTEOS: Int?,
            val framtidigTTNorsk: Int?,
            val harBoddArbeidUtland: Boolean,
            val nevnerTTBilateralProRata: Int?,
            val nevnerTTEOS: Int?,
            val nevnerTTNordiskKonv: Int?,
            val samletTTNordiskKonv: Int?,
            val tellerTTBilateralProRata: Int?,
            val tellerTTEOS: Int?,
            val tellerTTNordiskKonv: Int?,
        )

        data class Ufoeretrygd1(
            val beregningsgrunnlagBeloepAar: Kroner,
            val ufoeregrad: Int,
            val ufoeretidspunkt: LocalDate,
        )


        data class Yrkesskade1(
            val yrkesskadegrad: Int,
            val inntektVedSkadetidspunkt: Kroner,
            val beregningsgrunnlagBeloepAarYrkesskade: Kroner,
        )
    }
}
