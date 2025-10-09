package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderDto

fun createAvslagUttakFoerNormertPensjonsalderDto() =
    AvslagUttakFoerNormertPensjonsalderDto(
        saksbehandlerValg = AvslagUttakFoerNormertPensjonsalderDto.SaksbehandlerValg(false),
        pesysData = createAvslagUttakFoerNormertPensjonsalderAutoDto()
    )
