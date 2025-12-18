package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.alder.model.avslag.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg

fun createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto() =
    AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto()
    )
