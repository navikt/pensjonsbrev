package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OMSAvslagDTO

fun createOMSAvslagDTO() =
    OMSAvslagDTO(
        innhold = emptyList(),
        avdoedNavn = "Ola Nordmann"
    )