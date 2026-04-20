package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmLavereMinstesatsRedigerbarDto

fun createVedtakOmLavereMinstesatsRedigerbarDto() =
    VedtakOmLavereMinstesatsRedigerbarDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = VedtakOmLavereMinstesatsRedigerbarDto.PesysData(
            vedtakData = createVedtakOmLavereMinstesatsData(),
        )
    )
