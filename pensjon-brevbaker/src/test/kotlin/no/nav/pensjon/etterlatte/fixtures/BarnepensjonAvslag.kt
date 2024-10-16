package no.nav.pensjon.etterlatte.fixtures

import io.mockk.mockk
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagDTO

fun createBarnepensjonAvslagDTO() {
    val mockAvdoed = mockk<Avdoed>(name = "John Doe");

    BarnepensjonAvslagDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        bosattUtland = false,
        brukerUnder18Aar = false,
        erSluttbehandling = false,
        avdoed = mockAvdoed,
    )

}


