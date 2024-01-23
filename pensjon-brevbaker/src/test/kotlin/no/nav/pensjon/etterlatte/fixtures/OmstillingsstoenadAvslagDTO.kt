package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagDTO

fun createOmstillingsstoenadAvslagDTO() =
    OmstillingstoenadAvslagDTO(
        innhold = emptyList(),
        avdoedNavn = "Ola Nordmann"
    )