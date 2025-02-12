package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.SivilstandAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class UfoerOmregningEnsligDto(
    val opplysningerBruktIBeregningUT: OpplysningerBruktIBeregningUTDto,
    val orienteringOmRettigheterOgPlikter: OrienteringOmRettigheterUfoereDto,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val avdoed: Avdoed,
    val minsteytelseVedvirk_sats: Double?,
    val ufoeretrygdVedVirk: UfoeretrygdVedVirk,
    val beregnetUTPerMaaned_antallBeregningsperioderPaaVedtak: Int,
    val institusjonsoppholdVedVirk: Institusjon,
    val krav_virkningsDatoFraOgMed: LocalDate,
    val barnetilleggSaerkullsbarnGjeldende_erRedusertMotInntekt: Boolean,
    val inntektFoerUfoerhetVedVirk: InntektFoerUfoerhetVedVirk,
    val bruker: Bruker,
    val harBarnetillegg: Boolean,
    val barnetilleggSaerkullsbarnVedVirk: BarnetilleggSaerkullsbarnVedvirk?,
) : BrevbakerBrevdata {
    data class Avdoed(
        val navn: String,
        val ektefelletilleggOpphoert: Boolean,
        val sivilstand: SivilstandAvdoed,
        val harFellesBarnUtenBarnetillegg: Boolean,
    )

    data class UfoeretrygdVedVirk(
        val kompensasjonsgrad: Double,
        val totalUfoereMaanedligBeloep: Kroner,
        val erInntektsavkortet: Boolean,
        val harGradertUfoeretrygd: Boolean,
        val grunnbeloep: Kroner,
    )

    data class InntektFoerUfoerhetVedVirk(
        val oppjustertBeloep: Kroner,
        val beloep: Kroner,
        val erMinsteinntekt: Boolean,
        val erSannsynligEndret: Boolean,
    )

    data class Bruker(
        val borIAvtaleLand: Boolean,
        val borINorge: Boolean,
    )

    data class BarnetilleggSaerkullsbarnVedvirk(
        val barnTidligereSaerkullsbarn: List<String>,
        val barnOverfoertTilSaerkullsbarn: List<String>,
        val beloep: Kroner,
        val erRedusertMotInntekt: Boolean,
        val inntektBruktIAvkortning: Kroner,
        val fribeloepVedvirk: Kroner,
        val justeringsbeloepAar: Kroner,
        val inntektstak: Kroner,
    )
}
