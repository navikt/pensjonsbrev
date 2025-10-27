package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.model.alder.AlderspensjonRegelverkType
import no.nav.pensjon.brev.model.alder.avslag.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto

fun createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto() =
    AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto(
        regelverkType = AlderspensjonRegelverkType.AP2025
    )
