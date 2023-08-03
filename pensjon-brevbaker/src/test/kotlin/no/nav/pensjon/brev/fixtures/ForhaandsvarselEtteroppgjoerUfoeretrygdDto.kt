package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDto
import no.nav.pensjon.brevbaker.api.model.Kroner

fun createForhaandsvarselEtteroppgjoerUfoeretrygdDto() =
    ForhaandsvarselEtteroppgjoerUfoeretrygdDto(
        erNyttEtteroppgjoer = false,
        harTjentOver80prosentAvOIFU = false,
        kanSoekeOmNyInntektsgrense = false,
        oppjustertInntektFoerUfoerhet = Kroner(0),
        opplysningerOmEtteroppgjoret = Fixtures.create(),
        orienteringOmRettigheterUfoere = Fixtures.create(),
        // TODO: Include attachment OpplysningerOmBeregningen
    )

