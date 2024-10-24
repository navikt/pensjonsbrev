package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagRedigerbartUtfallDTO

fun createBarnepensjonAvslagRedigerbartUtfallDTO(): BarnepensjonAvslagRedigerbartUtfallDTO =
    BarnepensjonAvslagRedigerbartUtfallDTO(
        erSluttbehandling = true
    )