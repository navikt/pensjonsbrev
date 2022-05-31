package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import java.time.LocalDate

data class UfoerOmregningEnsligDto(
    val opplysningerBruktIBeregningUT: OpplysningerBruktIBeregningUTDto,
    val orienteringOmRettigheterOgPlikter: OrienteringOmRettigheterUfoereDto,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto,
    val avdod: Avdod,
    val minsteytelseVedvirk_sats: Double?,
    val ufoeretrygdVedVirk: UfoeretrygdVedVirk,
    val beregnetUTPerManed_antallBeregningsperioderPaVedtak: Int,
    val institusjonsoppholdVedVirk: Institusjon,
    val krav_virkningsDatoFraOgMed: LocalDate,
    val barnetilleggSaerkullsbarnGjeldende_erRedusertMotInntekt: Boolean,
    val inntektFoerUfoerhetVedVirk: InntektFoerUfoerhetVedVirk,
    val bruker: Bruker,
    val barnetilleggVedVirk: BarnetilleggVedVirk?,
) {
    constructor() : this(
        opplysningerBruktIBeregningUT = OpplysningerBruktIBeregningUTDto(),
        orienteringOmRettigheterOgPlikter = OrienteringOmRettigheterUfoereDto(),
        maanedligUfoeretrygdFoerSkatt = MaanedligUfoeretrygdFoerSkattDto(),
        minsteytelseVedvirk_sats = 0.0,
        avdod = Avdod(
            navn = "Avdod Person",
            ektefelletilleggOpphoert = false,
            sivilstand = Sivilstand.SAMBOER3_2,
            harFellesBarnUtenBarnetillegg = false,
        ),
        krav_virkningsDatoFraOgMed = LocalDate.of(2020, 1, 1),
        beregnetUTPerManed_antallBeregningsperioderPaVedtak = 0,
        institusjonsoppholdVedVirk = Institusjon.INGEN,
        ufoeretrygdVedVirk = UfoeretrygdVedVirk(
            kompensasjonsgrad = 0.5,
            totalUforeMaanedligBeloep = Kroner(5),
            erInntektsavkortet = false
        ),
        inntektFoerUfoerhetVedVirk = InntektFoerUfoerhetVedVirk(
            oppjustertBeloep = Kroner(0),
            beloep = Kroner(0),
            erMinsteinntekt = false,
            erSannsynligEndret = false
        ),
        barnetilleggSaerkullsbarnGjeldende_erRedusertMotInntekt = false,
        bruker = Bruker(
            borIAvtaleLand = false,
            borINorge = true,
        ),
        barnetilleggVedVirk = BarnetilleggVedVirk(
            barnetilleggSaerkullsbarnVedVirk = BarnetilleggSaerkullsbarnVedvirk(
                belop = Kroner(0),
                erRedusertMotInntekt = false,
                inntektBruktIAvkortning = Kroner(0),
                fribeloepVedvirk = Kroner(0),
                justeringsbeloepAr = Kroner(0),
                inntektstak = Kroner(0),
                barnTidligereSaerkullsbarn = listOf(
                    "Tidligere saerkullsbarn 1",
                    "Tidligere saerkullsbarn 2",
                    "Tidligere saerkullsbarn 3",
                ),
                barnOverfoertTilSaerkullsbarn = listOf(
                    "Overfoert til saerkullsbarn 1",
                    "Overfoert til saerkullsbarn 2",
                    "Overfoert til saerkullsbarn 3",
                ),
            ),
            barnetilleggGrunnlag = BarnetilleggGrunnlagVedVirk(
                erRedusertMotTak = false,
                prosentsatsGradertOverInntektFoerUfoer = 0,
                gradertOverInntektFoerUfoer = Kroner(0),
                erIkkeUtbetPgaTak = false,
                belopFoerReduksjon = Kroner(0),
                belopEtterReduksjon = Kroner(0),
            )
        )
    )


    data class Avdod(
        val navn: String,
        val ektefelletilleggOpphoert: Boolean,
        val sivilstand: Sivilstand,
        val harFellesBarnUtenBarnetillegg: Boolean,
    )

    data class UfoeretrygdVedVirk(
        val kompensasjonsgrad: Double,
        val totalUforeMaanedligBeloep: Kroner,
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
        val erRedusertMotTak: Boolean,
        val prosentsatsGradertOverInntektFoerUfoer: Int,
        val gradertOverInntektFoerUfoer: Kroner,
        val erIkkeUtbetPgaTak: Boolean,
        val belopFoerReduksjon: Kroner,
        val belopEtterReduksjon: Kroner,
    )

    data class BarnetilleggSaerkullsbarnVedvirk(
        val barnTidligereSaerkullsbarn: List<String>,
        val barnOverfoertTilSaerkullsbarn: List<String>,
        val belop: Kroner,
        val erRedusertMotInntekt: Boolean,
        val inntektBruktIAvkortning: Kroner,
        val fribeloepVedvirk: Kroner,
        val justeringsbeloepAr: Kroner,
        val inntektstak: Kroner,
    )
}