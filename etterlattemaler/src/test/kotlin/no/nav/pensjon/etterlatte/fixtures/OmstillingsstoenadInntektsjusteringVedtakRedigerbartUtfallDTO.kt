package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallData
import java.time.LocalDate

fun createOmstillingsstoenadInntektsjusteringVedtakRedigerbartUtfallDTO() = OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallDTO(
    data = OmstillingsstoenadVedtakInntektsjusteringRedigerbartUtfallData(
        inntektsbeloep = Kroner(20000),
        inntektsaar = 2025,
    )
)