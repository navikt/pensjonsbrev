package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDto
import no.nav.pensjon.brev.api.model.maler.Ufoeretrygd
import java.time.LocalDate

fun createOpphoererBarnetilleggAutoDto() =
    OpphoererBarnetilleggAutoDto(
        foedselsdatoPaaBarnetilleggOpphoert = LocalDate.of(2004, 6, 14),
        oensketVirkningsDato = LocalDate.now(),
        barnetilleggFellesbarn = null,

         /*   barnetilleggFellesbarn = BarnetilleggFellesbarn(
                antallFellesbarnInnvilget = 2,
                beloepFratrukketAnnenForeldersInntekt = Kroner(2500),
                beloepNettoFellesbarn = Kroner(5000),
                fradragFellesbarn = Kroner(2000),
                fribeloepFellesbarn = Kroner(1000),
                inntektAnnenForelder = Kroner(550000),
                inntektBruktIAvkortningFellesbarn = Kroner(375000),
                inntektstak = Kroner(320000),
                justeringsbeloepFellesbarn = Kroner(7500)
            ), */
        barnetilleggSaerkullsbarn = BarnetilleggSaerkullsbarn(
            antallSaerkullsbarnbarnInnvilget = 1,
            beloepNettoSaerkullsbarn = Kroner(10000),
            fradragSaerkullsbarn = Kroner(2500),
            fribeloepSaerkullsbarn = Kroner(14000),
            inntektBruktIAvkortningSaerkullsbarn = Kroner(8000),
            inntektstakSaerkullsbarn = Kroner(350000),
            justeringsbeloepSaerkullsbarn = Kroner(7500),
        ),
        brukerBorInorge = true,
        grunnbeloep = Kroner(98000),
        sivilstand = Sivilstand.SAMBOER1_5,
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