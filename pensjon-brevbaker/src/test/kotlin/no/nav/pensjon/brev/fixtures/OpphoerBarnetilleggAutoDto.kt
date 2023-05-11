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
            LocalDate.of(2003, 2, 5),
        ),
        oensketVirkningsDato = LocalDate.now(),
        barnetilleggFellesbarn = BarnetilleggFellesbarn(
            beloepBrutto = Kroner(8000),
            beloepNetto = Kroner(5000),
            brukersInntektBruktIAvkortning = Kroner(25000),
            fribeloep = Kroner(10000),
            gjelderFlereBarn = false,
            harFradrag = true,
            harFratrukketBeloepFraAnnenForelder = true,
            harJusteringsbeloep = true,
            inntektAnnenForelder = Kroner(550000),
            inntektstak = Kroner(320000),
            samletInntektBruktIAvkortning = Kroner(500000),
        ),
        barnetilleggSaerkullsbarn = BarnetilleggSaerkullsbarn(
            beloepBrutto = Kroner(0),
            beloepNetto = Kroner(0),
            brukersInntektBruktIAvkortning = Kroner(0),
            fribeloep = Kroner(0),
            gjelderFlereBarn = false,
            harFradrag = false,
            harJusteringsbeloep = false,
            inntektstak = Kroner(0),
        ),
        brukerBorInorge =true,
        grunnbeloep = Kroner(98000),
        sivilstand = Sivilstand.GIFT,
        ufoeretrygd = Ufoeretrygd(
            ektefelletilleggUtbeltalt = Kroner(125000),
            gjenlevendetilleggUtbetalt = null,
            harUtbetalingsgrad = false,
            ufoertrygdUtbetalt = Kroner(320000),
            utbetaltPerMaaned = Kroner(345000),
        ),
        maanedligUfoeretrygdFoerSkatt = Fixtures.create(),
        opplysningerBruktIBeregningUT = Fixtures.create(),
        orienteringOmRettigheterUfoere = Fixtures.create()
    )