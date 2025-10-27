package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.model.alder.avslag.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto

fun createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto() =
    AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto()
    )
