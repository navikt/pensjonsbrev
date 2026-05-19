package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagRedigerbartUtfallData

fun createBarnepensjonAvslagRedigerbartUtfallDTO(): BarnepensjonAvslagRedigerbartUtfallDTO =
    BarnepensjonAvslagRedigerbartUtfallDTO(
        data = BarnepensjonAvslagRedigerbartUtfallData(
            erSluttbehandling = true,
        )
    )