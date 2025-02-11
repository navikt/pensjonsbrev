package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallDTO

fun createOmstillingsstoenadInntektsjusteringVedtakRedigerbartUtfallDTO() =
    OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallDTO(
        inntektsbeloep = Kroner(20000),
        inntektsaar = 2025,
    )
