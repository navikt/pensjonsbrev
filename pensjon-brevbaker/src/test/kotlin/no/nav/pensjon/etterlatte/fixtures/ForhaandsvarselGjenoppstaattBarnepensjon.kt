package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.gjenoppstaatt.BarnepensjonForhaandsvarselGjenoppstaattDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.gjenoppstaatt.BarnepensjonForhaandsvarselGjenoppstaattRedigerbartUtfallDTO

fun createBarnepensjonForhaandsvarselGjenoppstaatt() = BarnepensjonForhaandsvarselGjenoppstaattDTO(
    innhold = createPlaceholderForRedigerbartInnhold(),
    beregning = lagBeregning(),
    erUnder18Aar = true,
    erBosattUtlandet = false,
)

fun createBarnepensjonForhaandsvarselGjenoppstaattRedigerbartUtfall() =
    BarnepensjonForhaandsvarselGjenoppstaattRedigerbartUtfallDTO(
        beregning = lagBeregning(),
        automatiskBehandla = false,
        erBosattUtlandet = false,
    )


