package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto

fun createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto() =
    AvslagGradsendringFoerNormertPensjonsalderFoerEttAarDto(
        saksbehandlerValg = EmptyBrevdata,
        pesysData = createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto()
    )
