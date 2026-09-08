package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsData

data class VedtakOmLavereMinstesatsRedigerbarDto(
    val vedtakData: VedtakOmLavereMinstesatsData,
) : FagsystemBrevdata
