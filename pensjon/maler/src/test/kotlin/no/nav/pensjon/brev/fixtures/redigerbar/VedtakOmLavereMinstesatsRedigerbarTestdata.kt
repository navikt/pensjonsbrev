package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmLavereMinstesatsRedigerbarDto

fun createVedtakOmLavereMinstesatsRedigerbarDto() =
    VedtakOmLavereMinstesatsRedigerbarDto(
        vedtakData = createVedtakOmLavereMinstesatsData(),
    )
