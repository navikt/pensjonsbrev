package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import java.time.LocalDate

data class MaanedligPensjonFoerSkattAlderspensjonDto(
    val krav: Krav,
    val alderspensjonGjeldende: AlderspensjonGjeldende,
    val alderspensjonPerManed: MaanedligPensjonFoerSkattDto.AlderspensjonPerManed
) {
    data class Krav(
        val virkDatoFom: LocalDate
    )

    data class AlderspensjonGjeldende(
        val regelverkType: AlderspensjonRegelverkType
    )
}