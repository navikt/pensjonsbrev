package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class OpplysningerBruktIBeregningenLegacyDto(
    val tabellUfoereOpplysninger: TabellufoereOpplysningerDto,
    val opplysningerOmBarnetillegg: OpplysningerOmBarnetilleggDto?,

    // Tittel
    val inntektsgrenseAar: Kroner,
    val inntektstak: Kroner,
    val beregnetUTPerMaanedGjeldendeVirkFom: LocalDate,
    val beregnetUTPerMaanedGjeldendeGrunnbeloep: Kroner,

    // strengt nødvendige ekstra variabler for legacy:
    val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor: List<TrygdetidNor>,
    val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeEOS: List<TrygdetidEOS>,
    val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral: List<TrygdetidBilateral>,
){

    data class TrygdetidNor(
        val fom: LocalDate,
        val tom: LocalDate?,
    )

    data class TrygdetidEOS(
        val land: String,
        val fom: LocalDate,
        val tom: LocalDate?,
    )

    data class TrygdetidBilateral(
        val land: String,
        val fom: LocalDate,
        val tom: LocalDate?,
    )

    data class TabellufoereOpplysningerDto(
        val ufoeretrygdGjeldende: OpplysningerBruktIBeregningUTDto.UfoeretrygdGjeldende,
        val yrkesskadeGjeldende: OpplysningerBruktIBeregningUTDto.YrkesskadeGjeldende?,
        val inntektFoerUfoereGjeldende: OpplysningerBruktIBeregningUTDto.InntektFoerUfoereGjeldende,
        val inntektsAvkortingGjeldende: OpplysningerBruktIBeregningUTDto.InntektsAvkortingGjeldende,
        val inntektsgrenseErUnderTak: Boolean,
        val beregnetUTPerManedGjeldende: OpplysningerBruktIBeregningUTDto.BeregnetUTPerManedGjeldende,
        val inntektEtterUfoereGjeldendeBeloep: Kroner?,
        val erUngUfoer: Boolean,
        val trygdetidsdetaljerGjeldende: OpplysningerBruktIBeregningUTDto.TrygdetidsdetaljerGjeldende,
        val barnetilleggGjeldende: OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende?,
        val harMinsteytelse: Boolean,
        val brukersSivilstand: Sivilstand,
        val borMedSivilstand: BorMedSivilstand?,
        val kravGjelderFoerstegangsbehandlingBosattUtland: Boolean,
        val antallAarOver1G: Int,
        val antallAarOverInntektIAvtaleland: Int,
        val ufoeretrygd_reduksjonsgrunnlag_gradertOppjustertIFU: Kroner,
        val beregningUfore_andelYtelseAvOIFU: Int,
        val beregningUfore_BeregningVirkningDatoFom: LocalDate,
        val beregningUfore_prosentsatsOIFUForTak: Int,
    )

    data class OpplysningerOmMinstetilleggDto(
        val minsteytelseGjeldendeSats: Double?,
        val ungUfoerGjeldende_erUnder20Aar: Boolean?,
        val ufoeretrygdGjeldende: OpplysningerBruktIBeregningUTDto.UfoeretrygdGjeldende,
        val inntektFoerUfoereGjeldende: OpplysningerBruktIBeregningUTDto.InntektFoerUfoereGjeldende,
        val inntektsgrenseErUnderTak: Boolean,
    )

    data class OpplysningerOmBarnetilleggDto(
        val barnetillegg: OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende,
        val anvendtTrygdetid: Int,
        val harYrkesskade: Boolean,
        val harKravaarsakEndringInntekt: Boolean,
        val fraOgMedDatoErNesteAar: Boolean,
    )
}
