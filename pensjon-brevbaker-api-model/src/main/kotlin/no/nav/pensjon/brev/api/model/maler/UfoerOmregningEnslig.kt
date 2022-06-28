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
    constructor() : this(
        opplysningerBruktIBeregningUT = OpplysningerBruktIBeregningUTDto(),
        orienteringOmRettigheterOgPlikter = OrienteringOmRettigheterUfoereDto(),
        maanedligUfoeretrygdFoerSkatt = MaanedligUfoeretrygdFoerSkattDto(),
        minsteytelseVedvirk_sats = 0.0,
        avdoed = Avdoed(
            navn = "Avdod Person",
            ektefelletilleggOpphoert = false,
            sivilstand = Sivilstand.SAMBOER3_2,
            harFellesBarnUtenBarnetillegg = false,
        ),
        krav_virkningsDatoFraOgMed = LocalDate.of(2020, 1, 1),
        beregnetUTPerMaaned_antallBeregningsperioderPaaVedtak = 0,
        institusjonsoppholdVedVirk = Institusjon.INGEN,
        ufoeretrygdVedVirk = UfoeretrygdVedVirk(
            kompensasjonsgrad = 0.5,
            totalUfoereMaanedligBeloep = Kroner(5),
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
                beloep = Kroner(0),
                erRedusertMotInntekt = false,
                inntektBruktIAvkortning = Kroner(0),
                fribeloepVedvirk = Kroner(0),
                justeringsbeloepAar = Kroner(0),
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
                erIkkeUtbetaltPgaTak = false,
                beloepFoerReduksjon = Kroner(0),
                beloepEtterReduksjon = Kroner(0),
            )
        )
    )


    data class Avdoed(
        val navn: String,
        val ektefelletilleggOpphoert: Boolean,
        val sivilstand: Sivilstand,
        val harFellesBarnUtenBarnetillegg: Boolean,
    ) {
        constructor(): this(
            navn = "Avdod Person",
            ektefelletilleggOpphoert = false,
            sivilstand = Sivilstand.SAMBOER3_2,
            harFellesBarnUtenBarnetillegg = false,
        )
    }

    data class UfoeretrygdVedVirk(
        val kompensasjonsgrad: Double,
        val totalUfoereMaanedligBeloep: Kroner,
        val erInntektsavkortet: Boolean,
    ) {
        constructor(): this(
            kompensasjonsgrad = 0.5,
            totalUfoereMaanedligBeloep = Kroner(5),
            erInntektsavkortet = false
        )
    }

    data class InntektFoerUfoerhetVedVirk(
        val oppjustertBeloep: Kroner,
        val beloep: Kroner,
        val erMinsteinntekt: Boolean,
        val erSannsynligEndret: Boolean
    ) {
        constructor(): this(
            oppjustertBeloep = Kroner(0),
            beloep = Kroner(0),
            erMinsteinntekt = false,
            erSannsynligEndret = false
        )
    }

    data class Bruker(
        val borIAvtaleLand: Boolean,
        val borINorge: Boolean,
    ) {
        constructor(): this(
            borIAvtaleLand = false,
            borINorge = true,
        )
    }

    data class BarnetilleggVedVirk(
        val barnetilleggGrunnlag: BarnetilleggGrunnlagVedVirk,
        val barnetilleggSaerkullsbarnVedVirk: BarnetilleggSaerkullsbarnVedvirk?,
    ) {
        constructor(): this(
            barnetilleggSaerkullsbarnVedVirk = BarnetilleggSaerkullsbarnVedvirk(),
            barnetilleggGrunnlag = BarnetilleggGrunnlagVedVirk()
        )
    }

    data class BarnetilleggGrunnlagVedVirk(
        val erRedusertMotTak: Boolean,
        val prosentsatsGradertOverInntektFoerUfoer: Int,
        val gradertOverInntektFoerUfoer: Kroner,
        val erIkkeUtbetaltPgaTak: Boolean,
        val beloepFoerReduksjon: Kroner,
        val beloepEtterReduksjon: Kroner,
    ) {
        constructor() : this(
            erRedusertMotTak = false,
            prosentsatsGradertOverInntektFoerUfoer = 0,
            gradertOverInntektFoerUfoer = Kroner(0),
            erIkkeUtbetaltPgaTak = false,
            beloepFoerReduksjon = Kroner(0),
            beloepEtterReduksjon = Kroner(0),
        )
    }

    data class BarnetilleggSaerkullsbarnVedvirk(
        val barnTidligereSaerkullsbarn: List<String>,
        val barnOverfoertTilSaerkullsbarn: List<String>,
        val beloep: Kroner,
        val erRedusertMotInntekt: Boolean,
        val inntektBruktIAvkortning: Kroner,
        val fribeloepVedvirk: Kroner,
        val justeringsbeloepAar: Kroner,
        val inntektstak: Kroner,
    ) {
        constructor() : this(
            beloep = Kroner(0),
            erRedusertMotInntekt = false,
            inntektBruktIAvkortning = Kroner(0),
            fribeloepVedvirk = Kroner(0),
            justeringsbeloepAar = Kroner(0),
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
        )
    }
}