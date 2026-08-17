package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmLavereMinstesatsRedigerbarDto

fun createVedtakOmLavereMinstesatsRedigerbarDto() =
    VedtakOmLavereMinstesatsRedigerbarDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = VedtakOmLavereMinstesatsRedigerbarDto.PesysData(
            vedtakData = createVedtakOmLavereMinstesatsData(),
        )
    )
