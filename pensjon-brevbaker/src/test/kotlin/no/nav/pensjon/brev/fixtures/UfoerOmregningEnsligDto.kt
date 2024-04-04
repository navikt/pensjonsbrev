package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createUfoerOmregningEnsligDto() =
    UfoerOmregningEnsligDto(
        opplysningerBruktIBeregningUT = Fixtures.create(),
        orienteringOmRettigheterOgPlikter = Fixtures.create(),
        maanedligUfoeretrygdFoerSkatt = Fixtures.create(),
        minsteytelseVedvirk_sats = 0.0,
        avdoed = UfoerOmregningEnsligDto.Avdoed(
            navn = "Avdod Person",
            ektefelletilleggOpphoert = false,
            sivilstand = SivilstandAvdoed.SAMBOER3_2,
            harFellesBarnUtenBarnetillegg = false,
        ),
        krav_virkningsDatoFraOgMed = LocalDate.of(2020, 1, 1),
        beregnetUTPerMaaned_antallBeregningsperioderPaaVedtak = 0,
        institusjonsoppholdVedVirk = Institusjon.INGEN,
        ufoeretrygdVedVirk = UfoerOmregningEnsligDto.UfoeretrygdVedVirk(
            kompensasjonsgrad = 0.5,
            totalUfoereMaanedligBeloep = Kroner(0),
            erInntektsavkortet = false,
            harGradertUfoeretrygd = false,
            grunnbeloep = Kroner(1234)
        ),
        inntektFoerUfoerhetVedVirk = UfoerOmregningEnsligDto.InntektFoerUfoerhetVedVirk(
            oppjustertBeloep = Kroner(0),
            beloep = Kroner(0),
            erMinsteinntekt = false,
            erSannsynligEndret = false
        ),
        barnetilleggSaerkullsbarnGjeldende_erRedusertMotInntekt = false,
        bruker = UfoerOmregningEnsligDto.Bruker(
            borIAvtaleLand = false,
            borINorge = true,
        ),
        barnetilleggSaerkullsbarnVedVirk = UfoerOmregningEnsligDto.BarnetilleggSaerkullsbarnVedvirk(
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
        harBarnetillegg = false,
    )