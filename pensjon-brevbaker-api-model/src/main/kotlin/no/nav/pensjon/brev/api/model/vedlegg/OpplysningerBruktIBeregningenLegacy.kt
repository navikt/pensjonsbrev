package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class OpplysningerBruktIBeregningenLegacyDto(
    val tabellUfoereOpplysninger: TabellufoereOpplysningerLegacyDto,
    val opplysningerOmBarnetillegg: OpplysningerOmBarnetilleggDto?,
    val PE: PE, // Legacy data
    // Tittel
    val inntektsgrenseAar: Kroner,
    val inntektstak: Kroner,
    val beregnetUTPerMaanedGjeldendeVirkFom: LocalDate,
    val beregnetUTPerMaanedGjeldendeGrunnbeloep: Kroner,
){

    data class TabellufoereOpplysningerLegacyDto(
        val ufoeretrygdGjeldende: OpplysningerBruktIBeregningUTDto.UfoeretrygdGjeldende,
        val yrkesskadeGjeldende: OpplysningerBruktIBeregningUTDto.YrkesskadeGjeldende?,
        val inntektFoerUfoereGjeldende: OpplysningerBruktIBeregningUTDto.InntektFoerUfoereGjeldende,
        val inntektsAvkortingGjeldende: OpplysningerBruktIBeregningUTDto.InntektsAvkortingGjeldende,
        val beregnetUTPerManedGjeldende: OpplysningerBruktIBeregningUTDto.BeregnetUTPerManedGjeldende,
        val inntektEtterUfoereGjeldendeBeloep: Kroner?,
        val erUngUfoer: Boolean,
        val trygdetidsdetaljerGjeldende: OpplysningerBruktIBeregningUTDto.TrygdetidsdetaljerGjeldende,
        val barnetilleggGjeldende: OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende?,
        val harMinsteytelse: Boolean,
        val brukersSivilstand: Sivilstand,
        val borMedSivilstand: BorMedSivilstand?,
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
