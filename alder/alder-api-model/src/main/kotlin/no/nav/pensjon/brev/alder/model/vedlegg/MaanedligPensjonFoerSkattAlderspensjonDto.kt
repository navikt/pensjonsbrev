package no.nav.pensjon.brev.alder.model.vedlegg

import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.VedleggData
import java.time.LocalDate

data class MaanedligPensjonFoerSkattAlderspensjonDto(
    val krav: Krav,
    val alderspensjonGjeldende: AlderspensjonGjeldende,
    val alderspensjonPerManed: List<MaanedligPensjonFoerSkattTabell.AlderspensjonPerManed>
) : VedleggData {
    data class Krav(
        val virkDatoFom: LocalDate
    )

    data class AlderspensjonGjeldende(
        val regelverkType: AlderspensjonRegelverkType
    )
}