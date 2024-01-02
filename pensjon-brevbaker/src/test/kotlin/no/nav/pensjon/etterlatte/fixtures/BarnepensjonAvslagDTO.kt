package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagDTO

fun createBarnepensjonAvslagDTO() =
    BarnepensjonAvslagDTO(
        innhold = emptyList(),
        bosattUtland = false,
        brukerUnder18Aar = false
    )
