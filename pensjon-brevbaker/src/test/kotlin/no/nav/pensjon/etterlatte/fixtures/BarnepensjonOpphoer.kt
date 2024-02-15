package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerRedigerbartUtfallDTO
import java.time.LocalDate
import java.time.Month

fun createBarnepensjonOpphoerDTO() = BarnepensjonOpphoerDTO(
    innhold = createPlaceholderForRedigerbartInnhold(),
    innholdForhaandsvarsel = createPlaceholderForRedigerbartInnhold(),
    bosattUtland = true,
    brukerUnder18Aar = true,
    feilutbetaling = FeilutbetalingType.FEILUTBETALING_MED_VARSEL,
    virkningsdato = LocalDate.of(2024, Month.MARCH, 1)
)

fun createBarnepensjonOpphoerRedigerbartUtfallDTO() = BarnepensjonOpphoerRedigerbartUtfallDTO(
    feilutbetaling = FeilutbetalingType.FEILUTBETALING_MED_VARSEL
)