package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakDataDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakRedigerbartUtfallDTO

fun createEtteroppgjoerVedtakDTO() =
    EtteroppgjoerVedtakDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        data = EtteroppgjoerVedtakDataDTO(bosattUtland = false)
    )

fun createEtteroppgjoerVedtakRedigerbartUtfallDTO() =
    EtteroppgjoerVedtakRedigerbartUtfallDTO()