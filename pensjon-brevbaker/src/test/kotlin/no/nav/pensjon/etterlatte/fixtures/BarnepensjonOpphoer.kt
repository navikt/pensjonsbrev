package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerRedigerbartUtfallDTO

fun createBarnepensjonOpphoerDTO() = BarnepensjonOpphoerDTO(
    innhold = createPlaceholderForRedigerbartInnhold(),
    innholdForhaandsvarsel = createPlaceholderForRedigerbartInnhold(),
    bosattUtland = true,
    brukerUnder18Aar = true,
    feilutbetaling = FeilutbetalingType.FEILUTBETALING_MED_VARSEL
)

fun createBarnepensjonOpphoerRedigerbartUtfallDTO() = BarnepensjonOpphoerRedigerbartUtfallDTO(
    feilutbetaling = FeilutbetalingType.FEILUTBETALING_MED_VARSEL
)