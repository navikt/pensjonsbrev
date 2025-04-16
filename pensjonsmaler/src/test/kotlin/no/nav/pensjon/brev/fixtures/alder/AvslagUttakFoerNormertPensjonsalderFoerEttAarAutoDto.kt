package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto

fun createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto() =
    AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto(
        regelverkType = AlderspensjonRegelverkType.AP2025
    )
