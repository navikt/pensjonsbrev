package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselBrevDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselDTO

fun createEtteroppgjoerForhaandsvarsel() =
    EtteroppgjoerForhaandsvarselBrevDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        data = EtteroppgjoerForhaandsvarselDTO(
            bosattUtland = false,
            norskInntekt = false,
            etteroppgjoersAar = 2024,
            rettsgebyrBeloep = Kroner(123),
            resultatType = "TILBAKEKREVING",
            inntekt = Kroner(321),
            faktiskInntekt = Kroner(123),
            avviksBeloep = Kroner(-3145)
        ),
    )