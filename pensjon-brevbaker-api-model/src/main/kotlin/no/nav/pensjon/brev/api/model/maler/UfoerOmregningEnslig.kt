package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import java.time.LocalDate

@Suppress("unused")
data class UfoerOmregningEnsligDto(
    val opplysningerBruktIBeregningUT: OpplysningerBruktIBeregningUTDto,
    val orienteringOmRettigheterOgPlikter: OrienteringOmRettigheterUfoereDto,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto,
    val avdoed: Avdoed,
    val minsteytelseVedvirk_sats: Double?,
    val ufoeretrygdVedVirk: UfoeretrygdVedVirk,
    val beregnetUTPerMaaned_antallBeregningsperioderPaaVedtak: Int,
    val institusjonsoppholdVedVirk: Institusjon,
    val krav_virkningsDatoFraOgMed: LocalDate,
    val barnetilleggSaerkullsbarnGjeldende_erRedusertMotInntekt: Boolean,
    val inntektFoerUfoerhetVedVirk: InntektFoerUfoerhetVedVirk,
    val bruker: Bruker,
    val barnetilleggVedVirk: BarnetilleggVedVirk?,
) {
    data class Avdoed(
        val navn: String,
        val ektefelletilleggOpphoert: Boolean,
        val sivilstand: Sivilstand,
        val harFellesBarnUtenBarnetillegg: Boolean,
    )

    data class UfoeretrygdVedVirk(
        val kompensasjonsgrad: Double,
        val totalUfoereMaanedligBeloep: Kroner,
        val erInntektsavkortet: Boolean,
    )

    data class InntektFoerUfoerhetVedVirk(
        val oppjustertBeloep: Kroner,
        val beloep: Kroner,
        val erMinsteinntekt: Boolean,
        val erSannsynligEndret: Boolean
    )

    data class Bruker(
        val borIAvtaleLand: Boolean,
        val borINorge: Boolean,
    )

    data class BarnetilleggVedVirk(
        val barnetilleggGrunnlag: BarnetilleggGrunnlagVedVirk,
        val barnetilleggSaerkullsbarnVedVirk: BarnetilleggSaerkullsbarnVedvirk?,
    )

    data class BarnetilleggGrunnlagVedVirk(
        val prosentsatsGradertOverInntektFoerUfoer: Int,
        val gradertOverInntektFoerUfoer: Kroner,
        val beloepFoerReduksjon: Kroner,
        val beloepEtterReduksjon: Kroner,
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