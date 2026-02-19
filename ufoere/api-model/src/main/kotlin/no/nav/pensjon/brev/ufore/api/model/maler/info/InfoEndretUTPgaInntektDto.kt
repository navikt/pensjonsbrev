package no.nav.pensjon.brev.ufore.api.model.maler.info

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner

data class InfoEndretUTPgaInntektDto(
    val loependeInntektsAvkortning: LoependeInntektsAvkortning,
    val uforetrygdInnevarendeAr: Uforetrygd
) : AutobrevData {
    data class Uforetrygd(
        val utbetalingsgrad: Int,
        val ufoeregrad: Int,
    )

    data class LoependeInntektsAvkortning(
        val forventetInntektAar: Kroner,
        val inntektsgrenseAar: Kroner,
    )
}
