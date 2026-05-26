package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEtterbetalingOpphor2026AutoDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

fun createVedtakOmEtterbetalingOpphor2026AutoDto() =
    VedtakOmEtterbetalingOpphor2026AutoDto(
        etterbetaling = Kroner(12345),
        hjemler = setOf("12-13", "22-12"),
    )