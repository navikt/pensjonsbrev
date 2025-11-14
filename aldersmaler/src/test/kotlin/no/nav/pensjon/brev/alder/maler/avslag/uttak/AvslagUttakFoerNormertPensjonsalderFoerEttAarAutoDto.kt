package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.avslag.AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto

fun createAvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto() =
    AvslagGradsendringFoerNormertPensjonsalderFoerEttAarAutoDto(
        regelverkType = AlderspensjonRegelverkType.AP2025
    )
