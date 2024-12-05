package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallDTO
import java.time.LocalDate

fun createOmstillingsstoenadInntektsjusteringVedtakRedigerbartUtfallDTO() = OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallDTO(
    inntektsbeloep = Kroner(20000),
    opphoerDato = LocalDate.of(2025, 11, 1),
)