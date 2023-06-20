package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerDto
import no.nav.pensjon.brev.api.model.vedlegg.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year
import java.time.LocalDate

fun createForhaandsvarselEtteroppgjoerDto() =
    ForhaandsvarselEtteroppgjoerDto(
        resultatEtteroppgjoer = Fixtures.create(),
        ufoeretrygdEtteroppgjoer = Fixtures.create()
    )

fun createResultatEtteroppgjoer() =
    ForhaandsvarselEtteroppgjoerDto.ResultatEtteroppgjoer(
        avviksbeloep = Kroner(0),
        endretPensjonOgAndreYtelserBruker = false,
        endretPensjonOgAndreYtelserEPS = false,
        endretPersonGrunnlagInntektBruker = false,
        endretPersonGrunnlagInntektEPS = false,
        harEtterbetaling = false,
        harTilbakePenger = false,
        tidligereEOIverksatt = false
    )

fun createUfoeretrygdEtteroppgjoer() =
    ForhaandsvarselEtteroppgjoerDto.UfoeretrygdEtteroppgjoer(
        aarPeriodeFom = Year(2022),
        inntektOverInntektstak = false,
        inntektsgrensebeloepAar = Kroner(0),
        oppjustertInntektFoerUfoere = Kroner(0),
        periodeFom = LocalDate.of(2022, 1, 1),
        ufoeregrad = 0
    )