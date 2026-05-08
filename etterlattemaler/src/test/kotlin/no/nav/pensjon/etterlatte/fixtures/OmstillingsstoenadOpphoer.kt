package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerData
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerRedigerbartUtfallData
import java.time.LocalDate

fun createOmstillingsstoenadOpphoerDTO() =
    OmstillingsstoenadOpphoerDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        data = OmstillingsstoenadOpphoerData(
            innholdForhaandsvarsel = createPlaceholderForRedigerbartInnhold(),
            bosattUtland = false,
            feilutbetaling = FeilutbetalingType.FEILUTBETALING_MED_VARSEL,
            virkningsdato = LocalDate.of(2024, 1, 1),
        ),
    )

fun createOmstillingsstoenadpphoerRedigerbartUtfallDTO() =
    OmstillingsstoenadOpphoerRedigerbartUtfallDTO(
        data = OmstillingsstoenadOpphoerRedigerbartUtfallData(
            feilutbetaling = FeilutbetalingType.FEILUTBETALING_MED_VARSEL,
        )
    )