package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderAP2016Dto

fun createAvslagUttakFoerNormertPensjonsalderAP2016Dto() =
    AvslagUttakFoerNormertPensjonsalderAP2016Dto(
        saksbehandlerValg = AvslagUttakFoerNormertPensjonsalderAP2016Dto.SaksbehandlerValg(true),
        pesysData = createAvslagUttakFoerNormertPensjonsalderAP2016AutoDto()
    )
