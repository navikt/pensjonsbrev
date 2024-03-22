package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInformasjonDoedsfallDTO

fun createBarnepensjonInformasjonDoedsfallDTO() =
    BarnepensjonInformasjonDoedsfallDTO(
        innhold = emptyList(),
        avdoedNavn = "Ola Nordmann",
        borIutland =  false,
        erOver18aar = false
    )
