package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadInntektsjusteringVedtakDTO

fun createOmstillingsstoenadInntektsjusteringVedtakDTO() = OmstillingsstoenadInntektsjusteringVedtakDTO(
    innhold = createPlaceholderForRedigerbartInnhold(),
    beregning = TODO(),
    omsRettUtenTidsbegrensning = TODO(),
    tidligereFamiliepleier = TODO(),
    inntektsaar = TODO(),
    innvilgetMindreEnnFireMndEtterDoedsfall = TODO(),
    harUtbetaling = TODO(),
    endringIUtbetaling = TODO(),
    virkningstidspunkt = TODO(),
    bosattUtland = TODO(),
)