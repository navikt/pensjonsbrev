package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderDto

fun createAvslagUttakFoerNormertPensjonsalderDto() =
    AvslagUttakFoerNormertPensjonsalderDto(
        saksbehandlerValg = AvslagUttakFoerNormertPensjonsalderDto.SaksbehandlerValg(false),
        pesysData = createAvslagUttakFoerNormertPensjonsalderAutoDto()
    )
