package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEtterbetalingOpphor2026AutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmEtterbetalingOpphor2026RedigerbarDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

fun createVedtakOmEtterbetalingOpphor2026AutoDto() =
    VedtakOmEtterbetalingOpphor2026AutoDto(
        etterbetaling = Kroner(12345),
        hjemler = setOf("12-13", "22-12"),
    )

fun createVedtakOmEtterbetalingOpphor2026RedigerbarDto() =
    VedtakOmEtterbetalingOpphor2026RedigerbarDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = VedtakOmEtterbetalingOpphor2026RedigerbarDto.PesysData(
            etterbetaling = Kroner(12345),
            hjemler = setOf("12-13", "22-12"),
        )
    )