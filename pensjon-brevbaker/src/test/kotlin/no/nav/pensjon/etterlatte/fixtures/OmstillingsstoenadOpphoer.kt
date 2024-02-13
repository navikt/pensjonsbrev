package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerRedigerbartUtfallDTO
import java.time.LocalDate

fun createOmstillingsstoenadOpphoerDTO() =
    OmstillingsstoenadOpphoerDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        innholdForhaandsvarsel = createPlaceholderForRedigerbartInnhold(),
        bosattUtland = false,
        feilutbetaling = FeilutbetalingType.FEILUTBETALING_MED_VARSEL
    )

fun createOmstillingsstoenadpphoerRedigerbartUtfallDTO() =
    OmstillingsstoenadOpphoerRedigerbartUtfallDTO(
        virkningsdato = LocalDate.of(2024, 1, 1),
        feilutbetaling = FeilutbetalingType.FEILUTBETALING_MED_VARSEL
    )