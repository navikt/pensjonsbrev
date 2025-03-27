package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVarselDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVarselData
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVarselRedigerbartBrevDTO


fun createEtteroppgjoerVarselRedigerbartBrevDTO(): EtteroppgjoerVarselRedigerbartBrevDTO =
    EtteroppgjoerVarselRedigerbartBrevDTO(
        type = "WOW"
    )

fun createEtteroppgjoerVarselDTO(): EtteroppgjoerVarselDTO = EtteroppgjoerVarselDTO(
    innhold = emptyList(),
    data = EtteroppgjoerVarselData(
        type = "et varsel"
    ),
)