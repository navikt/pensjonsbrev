package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.ManueltBrevMedTittelDTO

fun createTomMalInformasjonsbrev() =
    ManueltBrevMedTittelDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        tittel = "Dette er et informasjonsbrev",
    )
