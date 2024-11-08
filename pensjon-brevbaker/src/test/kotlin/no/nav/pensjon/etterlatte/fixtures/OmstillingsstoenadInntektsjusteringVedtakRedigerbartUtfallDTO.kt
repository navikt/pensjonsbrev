package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallDTO
import java.time.LocalDate

fun createOmstillingsstoenadInntektsjusteringVedtakRedigerbartUtfallDTO() = OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallDTO(
    innhold = createPlaceholderForRedigerbartInnhold(),
    opphoerFoerDesemberNesteAar = false,
    inntektsbeloep = 20000,
    opphoerDato = LocalDate.of(2024, 1,1),
)