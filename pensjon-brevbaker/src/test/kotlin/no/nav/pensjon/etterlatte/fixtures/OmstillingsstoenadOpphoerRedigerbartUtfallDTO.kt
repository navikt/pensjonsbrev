package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerRedigerbartUtfallDTO
import java.time.LocalDate

fun createOmstillingsstoenadpphoerRedigerbartUtfallDTO() =
    OmstillingsstoenadOpphoerRedigerbartUtfallDTO(
        virkningsdato = LocalDate.of(2024, 1, 1)
    )