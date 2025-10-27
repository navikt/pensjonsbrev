package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.Vedlegg
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattTabell.AlderspensjonPerManed
import java.time.LocalDate

data class MaanedligPensjonFoerSkattAlderspensjonDto(
    val krav: Krav,
    val alderspensjonGjeldende: AlderspensjonGjeldende,
    val alderspensjonPerManed: List<AlderspensjonPerManed>
) : Vedlegg {
    data class Krav(
        val virkDatoFom: LocalDate
    )

    data class AlderspensjonGjeldende(
        val regelverkType: AlderspensjonRegelverkType
    )
}