package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselBrevDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselRedigerbartBrevDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerResultatType

fun createEtteroppgjoerForhaandsvarselBrevDTO() =
    EtteroppgjoerForhaandsvarselBrevDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        data = EtteroppgjoerForhaandsvarselDTO(
            bosattUtland = true,
            norskInntekt = true,
            etteroppgjoersAar = 2024,
            rettsgebyrBeloep = Kroner(1234),
            resultatType = EtteroppgjoerResultatType.IKKE_ETTEROPPGJOER,
            inntekt = Kroner(321),
            faktiskInntekt = Kroner(123),
            avviksBeloep = Kroner(-3145)
        ),
    )

fun createEtteroppgjoerForhaandsvarselRedigerbartBrevDTO() =
    EtteroppgjoerForhaandsvarselRedigerbartBrevDTO(
        type = "test"
    )