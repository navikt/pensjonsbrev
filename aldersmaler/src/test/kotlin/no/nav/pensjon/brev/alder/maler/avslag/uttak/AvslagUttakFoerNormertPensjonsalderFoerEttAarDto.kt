package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.alder.model.avslag.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto

fun createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto() =
    AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto()
    )
