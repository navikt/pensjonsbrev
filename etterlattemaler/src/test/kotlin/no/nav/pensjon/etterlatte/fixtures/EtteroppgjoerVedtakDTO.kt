package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakDataDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakRedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakRedigerbartUtfallInnholdDTO
import java.time.LocalDate

fun createEtteroppgjoerVedtakDTO() =
    EtteroppgjoerVedtakDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        data = EtteroppgjoerVedtakDataDTO(bosattUtland = false)
    )

fun createEtteroppgjoerVedtakRedigerbartUtfallBrevDTO() =
    EtteroppgjoerVedtakRedigerbartUtfallBrevDTO(
        data = EtteroppgjoerVedtakRedigerbartUtfallInnholdDTO(
            etteroppgjoersAar = 2024,
            forhaandsvarselSendtDato = LocalDate.of(2024,6,25),
            mottattSvarDato = LocalDate.of(2024, 7,15)
        )
    )