package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.NormertPensjonsalder
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createAvslagUttakFoerNormertPensjonsalderAutoDto() =
    AvslagUttakFoerNormertPensjonsalderAutoDto(
        minstePensjonssats = Kroner(215000),
        uttaksgrad = 80,
        normertPensjonsalder = NormertPensjonsalder(
            aar = 67,
            maaneder = 2
        ),
        virkFom = LocalDate.of(2025, 2, 1),
        totalPensjon = Kroner(200000),
        afpBruktIBeregning = true
    )
