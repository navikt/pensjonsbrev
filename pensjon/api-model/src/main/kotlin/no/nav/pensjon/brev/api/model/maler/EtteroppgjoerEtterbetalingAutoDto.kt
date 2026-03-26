package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto

@Suppress("unused")
data class EtteroppgjoerEtterbetalingAutoDto(
    val pe: PEgruppe10,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
): AutobrevData
