package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.*

fun createOmstillingsstoenadInformasjonDoedsfallDto() =
    OmstillingstoenadInformasjonDoedsfallDTO(
        avdoedNavn = "Ola Nordmann",
        borIutland = false
    )