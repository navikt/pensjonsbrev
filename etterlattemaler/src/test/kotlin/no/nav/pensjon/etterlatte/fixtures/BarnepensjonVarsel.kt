package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.varsel.BarnepensjonVarselDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.varsel.BarnepensjonVarselRedigerbartUtfallDTO

fun createBarnepensjonVarsel() =
    BarnepensjonVarselDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        beregning = lagBeregning(),
        erUnder18Aar = true,
        erBosattUtlandet = false,
    )

fun createBarnepensjonVarselRedigerbartUtfall() =
    BarnepensjonVarselRedigerbartUtfallDTO(
        automatiskBehandla = false,
        erBosattUtlandet = false,
    )
