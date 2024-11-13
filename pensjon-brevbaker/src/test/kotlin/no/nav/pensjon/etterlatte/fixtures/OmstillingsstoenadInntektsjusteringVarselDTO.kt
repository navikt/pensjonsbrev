package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadInntektsjusteringVarselDTO
import java.time.LocalDate

fun createOmstillingsstoenadInntektsjusteringVarselDTO() = OmstillingsstoenadInntektsjusteringVarselDTO(
    innhold = createPlaceholderForRedigerbartInnhold(),
    inntektsaar = 2025,
    bosattUtland = true,
    virkningstidspunkt = LocalDate.of(2025, 1, 1),
)