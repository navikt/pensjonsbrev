package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDto
import no.nav.pensjon.brev.api.model.maler.Ufoeretrygd
import java.time.LocalDate

fun createOpphoererBarnetilleggAutoDto()=
    OpphoererBarnetilleggAutoDto(
        foedselsdatoPaaBarnetilleggOpphoert = LocalDate.of(2004, 6, 14),
        oensketVirkningsDato = LocalDate.now(),
        barnetilleggSaerkullsbarn = null,

        barnetilleggFellesbarn = BarnetilleggFellesbarn(
            antallBarnInnvilget = 2,
            beloepFratrukketAnnenForeldersInntekt = Kroner(2500),
            beloepNetto = Kroner(8000),
            fradrag = Kroner(2000),
            fribeloep = Kroner(1000),
            inntektAnnenForelder = Kroner(550000),
            inntektBruktIAvkortning = Kroner(375000),
            inntektstak = Kroner(320000),
            justeringsbeloep = Kroner(7500)
        ),
    /*    barnetilleggSaerkullsbarn = BarnetilleggSaerkullsbarn(
            antallBarnInnvilget = 1,
            beloepFratrukketAnnenForeldersInntekt = Kroner(2500),
            beloepNetto = Kroner(10000),
            fribeloep = Kroner(14000),
            inntektBruktIAvkortning = Kroner(8000),
            inntektstak = Kroner(350000),
            justeringsbeloep = Kroner(7500)
        ), */
        brukerBorInorge = true,
        grunnbeloep = Kroner(98000),
        sivilstand = Sivilstand.GIFT,
        ufoeretrygd = Ufoeretrygd(
            ufoertrygdUtbetalt = 80,
            utbetaltPerMaaned = Kroner(345000),
            ektefelletilleggUtbeltalt = Kroner(125000),
            gjenlevendetilleggUtbetalt = null,
            harUtbetalingsgrad = false
            ),
        maanedligUfoeretrygdFoerSkatt = Fixtures.create(),
        opplysningerBruktIBeregningUT = Fixtures.create(),
        orienteringOmRettigheterUfoere = Fixtures.create()
    )