package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerAutoDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year

fun createForhaandsvarselEtteroppgjoerDto() =
    ForhaandsvarselEtteroppgjoerAutoDto(
        // TODO: Include attachment BeregningAvEtteroppgjoeret
        // TODO: Include attachment OpplysningerOmBeregningen
        // TODO: Include attachment PraktiskInformasjonEtteroppgjoeret
        orienteringOmRettigheterUfoere = Fixtures.create(),
        resultatEtteroppgjoer = Fixtures.create(),
        ufoeretrygdEtteroppgjoer = Fixtures.create(),
    )

fun createResultatEtteroppgjoer() =
    ForhaandsvarselEtteroppgjoerAutoDto.ResultatEtteroppgjoer(
        avviksbeloep = Kroner(25000),
        harNyttEtteroppgjoer = false,
    )

fun createUfoeretrygdEtteroppgjoer() =
    ForhaandsvarselEtteroppgjoerAutoDto.UfoeretrygdEtteroppgjoer(
        aarPeriodeFom = Year(2022),
        hoeyesteInntektsgrense = Kroner(0),
        inntektOverInntektstak = false,
        oppjustertInntektFoerUfoerhet = Kroner(0),
        ufoeregrad = 0,
        sumInntektUt = Kroner(0),
    )