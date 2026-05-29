package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagData

fun createBarnepensjonAvslagDTO(): BarnepensjonAvslagDTO =
    BarnepensjonAvslagDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        data = BarnepensjonAvslagData(
            brukerUnder18Aar = false,
            bosattUtland = false,
        ),
    )
