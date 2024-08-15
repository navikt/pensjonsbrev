package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInnhentingAvOpplysningerDTO

fun createBarnepensjonInnhentingAvOpplysningerDTO() =
    BarnepensjonInnhentingAvOpplysningerDTO(
        erOver18aar = false,
        borIUtlandet = true,
    )
