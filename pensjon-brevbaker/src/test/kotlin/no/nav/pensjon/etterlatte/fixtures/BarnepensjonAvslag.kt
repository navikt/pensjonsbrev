package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagDTO

fun createBarnepensjonAvslagDTO() {
    BarnepensjonAvslagDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        bosattUtland = false,
        brukerUnder18Aar = false,
        erSluttbehandling = false,
        avdoed = null,
    )

}


