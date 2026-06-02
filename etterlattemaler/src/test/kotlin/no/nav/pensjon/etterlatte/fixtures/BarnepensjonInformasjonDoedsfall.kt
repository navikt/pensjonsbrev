package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInformasjonDoedsfallDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInformasjonDoedsfallData

fun createBarnepensjonInformasjonDoedsfallDTO() =
    BarnepensjonInformasjonDoedsfallDTO(
        data = BarnepensjonInformasjonDoedsfallData(
            avdoedNavn = "Ola Nordmann",
            borIutland = false,
            erOver18aar = false,
        )
    )
