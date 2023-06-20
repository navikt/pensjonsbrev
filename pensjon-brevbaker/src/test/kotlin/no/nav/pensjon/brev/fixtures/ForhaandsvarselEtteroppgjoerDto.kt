package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerAutoDto
import no.nav.pensjon.brev.api.model.vedlegg.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year

fun createForhaandsvarselEtteroppgjoerDto() =
    ForhaandsvarselEtteroppgjoerAutoDto(
        resultatEtteroppgjoer = Fixtures.create(),
        ufoeretrygdEtteroppgjoer = Fixtures.create()
    )

fun createResultatEtteroppgjoer() =
    ForhaandsvarselEtteroppgjoerAutoDto.ResultatEtteroppgjoer(
        avviksbeloep = Kroner(25000),
        harEtterbetaling = false,
        harTilbakePenger = false,
        harNyttEtteroppgjoer = false,
    )

fun createUfoeretrygdEtteroppgjoer() =
    ForhaandsvarselEtteroppgjoerAutoDto.UfoeretrygdEtteroppgjoer(
        aarPeriodeFom = Year(2022),
        inntektOverInntektstak = false,
        inntektsgrensebeloepAar = Kroner(0),
        oppjustertInntektFoerUfoerhet = Kroner(0),
        ufoeregrad = 0
    )