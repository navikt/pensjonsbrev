package no.nav.pensjon.brev.fixtures.alder

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderDto

fun createAvslagUttakFoerNormertPensjonsalderDto() =
    AvslagUttakFoerNormertPensjonsalderDto(
        saksbehandlerValg = lagSaksbehandlervalg("visInfoOmUttakFoer67" to false),
        pesysData = createAvslagUttakFoerNormertPensjonsalderAutoDto()
    )
