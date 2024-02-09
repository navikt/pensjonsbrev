package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerRedigerbartUtfallDTO
import java.time.LocalDate

fun createOmstillingsstoenadOpphoerDTO() =
    OmstillingsstoenadOpphoerDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        bosattUtland = false
    )

fun createOmstillingsstoenadpphoerRedigerbartUtfallDTO() =
    OmstillingsstoenadOpphoerRedigerbartUtfallDTO(
        virkningsdato = LocalDate.of(2024, 1, 1)
    )