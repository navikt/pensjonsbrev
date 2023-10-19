package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OMSAvslagDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OMSOpphoerDTO

fun createOMSAvslagDTO() =
    OMSAvslagDTO(
        innhold = emptyList(),
        avdoedNavn = "Ola Nordmann"
    )