package no.nav.pensjon.brev.fixtures.alder

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderAP2016Dto

fun createAvslagUttakFoerNormertPensjonsalderAP2016Dto() =
    AvslagUttakFoerNormertPensjonsalderAP2016Dto(
        saksbehandlerValg = lagSaksbehandlervalg("visInfoOmUttakFoer67" to true),
        pesysData = createAvslagUttakFoerNormertPensjonsalderAP2016AutoDto()
    )
