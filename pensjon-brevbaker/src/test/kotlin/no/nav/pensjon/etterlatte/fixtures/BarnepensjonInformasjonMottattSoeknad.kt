package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonMottattSoeknadDTO

fun createBarnepensjonInformasjonMottattSoeknadDTO() =
    BarnepensjonMottattSoeknadDTO(
        bosattUtland =  true,
        erOver18aar = false,
        borINorgeEllerIkkeAvtaleland = false
    )
