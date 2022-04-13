package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligeUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterDto
import java.time.LocalDate

//TODO flytt inn i api-model.
data class UfoerOmregningEnsligDto(
    val opplysningerBruktIBeregningUT: OpplysningerBruktIBeregningUTDto,
    val orienteringOmRettigheterOgPlikter: OrienteringOmRettigheterDto,
    val maanedligeUfoeretrygdFoerSkattDto: MaanedligeUfoeretrygdFoerSkattDto,
    val avdod: Avdod,
    val minsteytelseVedvirk: MinsteytelseVedvirk?,
    val ufoeretrygdVedVirk: UfoeretrygdVedVirk,
    val beregnetUTPerManed_antallBeregningsperioderPaVedtak: Int,
    val institusjonsoppholdVedVirk: Institusjon,
    val erSannsynligEndret_inntektForUfoereVedVirk: Boolean,
    val krav: Krav,
    val barnetilleggSaerkullsbarnGjeldende_erRedusertMotInntekt: Boolean,
    val inntektFoerUfoerhetVedVirk: InntektFoerUfoerhetVedVirk,
    val bruker: Bruker,
    val barnetilleggVedVirk: BarnetilleggVedVirk?,
) {
    constructor() : this(
        opplysningerBruktIBeregningUT = OpplysningerBruktIBeregningUTDto(),
        orienteringOmRettigheterOgPlikter = OrienteringOmRettigheterDto(),
        maanedligeUfoeretrygdFoerSkattDto = MaanedligeUfoeretrygdFoerSkattDto(),
        erSannsynligEndret_inntektForUfoereVedVirk = false,
        minsteytelseVedvirk = MinsteytelseVedvirk(sats = 0.0),
        avdod = Avdod(
            navn = "Avdod Person",
            ektefelletilleggOpphoert = false,
            sivilstand = Sivilstand.SAMBOER3_2,
            harFellesBarnUtenBarnetillegg = false,
        ),
        krav = Krav(
            virkedatoFraOgMed = LocalDate.of(2020, 1, 1)
        ),
        beregnetUTPerManed_antallBeregningsperioderPaVedtak = 0,
        institusjonsoppholdVedVirk = Institusjon.INGEN,
        ufoeretrygdVedVirk = UfoeretrygdVedVirk(
            kompensasjonsgrad = 0.5,
            totalUforeMaanedligBeloep = 5,
            erInntektsavkortet = false
        ),
        inntektFoerUfoerhetVedVirk = InntektFoerUfoerhetVedVirk(
            oppjustertBeloep = 0, beloep = 0, erMinsteinntekt = false
        ),
        barnetilleggSaerkullsbarnGjeldende_erRedusertMotInntekt = false,
        bruker = Bruker(
            borIAvtaleLand = false,
            borINorge = true,
        ),
        barnetilleggVedVirk = BarnetilleggVedVirk(
            barnetilleggSaerkullsbarnVedVirk = BarnetilleggSaerkullsbarnVedvirk(
                belop = 0,
                erRedusertMotInntekt = false,
                inntektBruktIAvkortning = 0,
                fribeloepVedvirk = 0,
                justeringsbeloepAr = 0,
                inntektstak = 0,
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
                prosentsatsGradertOverInntektFoerUfoer = 0.0,
                gradertOverInntektFoerUfoer = 0,
                erIkkeUtbetPgaTak = false,
                belopFoerReduksjon = 0,
                belopEtterReduksjon = 0,
            )
        )

    )


    data class MinsteytelseVedvirk(val sats: Double)
    data class Avdod(
        val navn: String,
        val ektefelletilleggOpphoert: Boolean,
        val sivilstand: Sivilstand,
        val harFellesBarnUtenBarnetillegg: Boolean,
    )

    data class Krav(val virkedatoFraOgMed: LocalDate)
    data class UfoeretrygdVedVirk(
        val kompensasjonsgrad: Double,
        val totalUforeMaanedligBeloep: Int,
        val erInntektsavkortet: Boolean,
    )

    data class InntektFoerUfoerhetVedVirk(val oppjustertBeloep: Int, val beloep: Int, val erMinsteinntekt: Boolean)

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
        val prosentsatsGradertOverInntektFoerUfoer: Double,
        val gradertOverInntektFoerUfoer: Int,
        val erIkkeUtbetPgaTak: Boolean,
        val belopFoerReduksjon: Int,
        val belopEtterReduksjon: Int,
    )

    data class BarnetilleggSaerkullsbarnVedvirk(
        val barnTidligereSaerkullsbarn: List<String>,
        val barnOverfoertTilSaerkullsbarn: List<String>,
        val belop: Int,
        val erRedusertMotInntekt: Boolean,
        val inntektBruktIAvkortning: Int,
        val fribeloepVedvirk: Int,
        val justeringsbeloepAr: Int,
        val inntektstak: Int,
    )
}