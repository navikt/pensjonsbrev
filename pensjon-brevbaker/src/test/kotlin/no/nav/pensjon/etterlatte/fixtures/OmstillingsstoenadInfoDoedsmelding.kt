package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.*

fun createOmstillingsstoenadInformasjonDoedsfallDto() =
    OmstillingstoenadInformasjonDoedsfallDTO(
        innhold = emptyList(),
        avdoedNavn = "Ola Nordmann",
    )