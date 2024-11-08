package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadInntektsjusteringVarselDTO

fun createOmstillingsstoenadInntektsjusteringVarselDTO() = OmstillingsstoenadInntektsjusteringVarselDTO(
    innhold = createPlaceholderForRedigerbartInnhold(),
    inntektsaar = TODO(),
    bosattUtland = TODO(),
    virkningstidspunkt = TODO(),
    avsenderEnhet = TODO(),
)