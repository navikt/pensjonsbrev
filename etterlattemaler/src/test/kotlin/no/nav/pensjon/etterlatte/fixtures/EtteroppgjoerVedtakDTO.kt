package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerResultatType
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakBrevDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakDataDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakRedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakRedigerbartUtfallInnholdDTO
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerGrunnlagDTO
import java.time.LocalDate
import java.time.YearMonth

fun createEtteroppgjoerVedtakBrevDTO() =
    EtteroppgjoerVedtakBrevDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        data = EtteroppgjoerVedtakDataDTO(
            bosattUtland = true,
            etteroppgjoersAar = 2024,
            resultatType = EtteroppgjoerResultatType.TILBAKEKREVING,
            inntekt = Kroner(321),
            faktiskInntekt = Kroner(4444),
            avviksBeloep = Kroner(0),
            grunnlag = EtteroppgjoerGrunnlagDTO(
                YearMonth.of(2024, 1),
                YearMonth.of(2024, 12),
                12,
                Kroner(4),
                Kroner(40),
                Kroner(400),
                Kroner(4000)
            ),
            vedleggInnhold = emptyList(),
            utbetaltBeloep = Kroner(4444)
        )
    )

fun createEtteroppgjoerVedtakRedigerbartUtfallBrevDTO() =
    EtteroppgjoerVedtakRedigerbartUtfallBrevDTO(
        data = EtteroppgjoerVedtakRedigerbartUtfallInnholdDTO(
            etteroppgjoersAar = 2024,
            forhaandsvarselSendtDato = LocalDate.of(2024,6,25),
            mottattSvarDato = LocalDate.of(2024, 7,15)
        )
    )