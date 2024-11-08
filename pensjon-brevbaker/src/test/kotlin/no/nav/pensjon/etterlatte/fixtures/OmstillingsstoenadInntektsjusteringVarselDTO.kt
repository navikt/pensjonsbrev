package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.NAVEnhet
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadInntektsjusteringVarselDTO
import java.time.LocalDate

fun createOmstillingsstoenadInntektsjusteringVarselDTO() = OmstillingsstoenadInntektsjusteringVarselDTO(
    innhold = createPlaceholderForRedigerbartInnhold(),
    inntektsaar = 2025,
    bosattUtland = false,
    virkningstidspunkt = LocalDate.of(2025, 1, 1),
    avsenderEnhet = NAVEnhet("test", "test", Telefonnummer("test")),
)