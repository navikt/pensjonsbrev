package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.AutobrevData

data class EPSEndringIBarnetilleggAutoDto(
    val vedtakData: EPSEndringIBarnetilleggData,
) : AutobrevData