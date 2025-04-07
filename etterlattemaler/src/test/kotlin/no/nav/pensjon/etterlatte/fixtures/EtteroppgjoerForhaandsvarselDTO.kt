package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerUtbetalingDTO
import java.time.LocalDate

fun createEtteroppgjoerForhaandsvarsel() =
    EtteroppgjoerForhaandsvarselDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        bosattUtland = false,
        norskInntekt = false,
        etteroppgjoersAar = 2024,
        rettsgebyrBeloep = Kroner(123),
        resultatType = "TILBAKEKREVING",
        dagensDato = LocalDate.now(),
        utbetalingData = EtteroppgjoerUtbetalingDTO(
            inntekt = Kroner(321),
            faktiskInntekt = Kroner(123),
            avviksBeloep = Kroner(-3145)
        )
    )