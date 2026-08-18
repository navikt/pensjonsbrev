package no.nav.pensjon.brev.fixtures.alder

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.alder.model.avslag.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto

fun createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto() =
    AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto()
    )
