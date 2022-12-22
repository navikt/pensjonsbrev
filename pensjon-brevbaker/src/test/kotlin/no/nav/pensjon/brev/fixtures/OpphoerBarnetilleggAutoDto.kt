package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDto
import no.nav.pensjon.brev.api.model.maler.Ufoeretrygd
import java.time.LocalDate

fun createOpphoerBarnetilleggAutoDto() =
    OpphoerBarnetilleggAutoDto(
        foedselsdatoPaaBarnMedOpphoertBarnetillegg = listOf(
            LocalDate.of(2004, 6, 14),
            LocalDate.of(2001, 1, 2),
        ),
        oensketVirkningsDato = LocalDate.now(),
        barnetilleggFellesbarn = BarnetilleggFellesbarn(
            gjelderFlereBarn = true,
            harFradrag = true,
            harFratrukketBeloepFraAnnenForelder = true,
            beloepBrutto = Kroner(8000),
            beloepNetto = Kroner(5000),
            fribeloep = Kroner(10000),
            inntektAnnenForelder = Kroner(550000),
            inntektBruktIAvkortning = Kroner(375000),
            inntektstak = Kroner(320000),
            harJusteringsbeloep = true,
            utbetalt = false,
        ),
        barnetilleggSaerkullsbarn = BarnetilleggSaerkullsbarn(
            gjelderFlereBarn = true,
            harFradrag = true,
            beloepBrutto = Kroner(10000),
            beloepNetto = Kroner(5000),
            fribeloep = Kroner(14000),
            inntektBruktIAvkortning = Kroner(8000),
            inntektstak = Kroner(350000),
            harJusteringsbeloep = true,
            utbetalt = true,
        ),
        brukerBorInorge =true,
        grunnbeloep = Kroner(98000),
        sivilstand = Sivilstand.SAMBOER1_5,
        ufoeretrygd = Ufoeretrygd(
            ufoertrygdUtbetalt = Kroner(80),
            utbetaltPerMaaned = Kroner(345000),
            ektefelletilleggUtbeltalt = Kroner(125000),
            gjenlevendetilleggUtbetalt = null,
            harUtbetalingsgrad = false
        ),
        maanedligUfoeretrygdFoerSkatt = Fixtures.create(),
        opplysningerBruktIBeregningUT = Fixtures.create(),
        orienteringOmRettigheterUfoere = Fixtures.create()
    )