package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.VedleggBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattTabell.AlderspensjonPerManed
import java.time.LocalDate

data class MaanedligPensjonFoerSkattAlderspensjonDto(
    val krav: Krav,
    val alderspensjonGjeldende: AlderspensjonGjeldende,
    val alderspensjonPerManed: List<AlderspensjonPerManed>
) : VedleggBrevdata {
    data class Krav(
        val virkDatoFom: LocalDate
    )

    data class AlderspensjonGjeldende(
        val regelverkType: AlderspensjonRegelverkType
    )
}