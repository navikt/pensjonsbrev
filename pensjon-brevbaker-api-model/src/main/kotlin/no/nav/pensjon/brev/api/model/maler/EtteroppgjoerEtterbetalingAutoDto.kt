package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.maler.AutobrevData

@Suppress("unused")
data class EtteroppgjoerEtterbetalingAutoDto(
    val pe: PE,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
): AutobrevData
