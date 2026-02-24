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
            resultatType = EtteroppgjoerResultatType.ETTERBETALING,
            stoenad = Kroner(321),
            faktiskStoenad = Kroner(4444),
            avviksBeloep = Kroner(-12340),
            grunnlag = EtteroppgjoerGrunnlagDTO(
                YearMonth.of(2024, 1),
                YearMonth.of(2024, 12),
                12,
                Kroner(0),
                Kroner(0),
                Kroner(0),
                Kroner(0),
                Kroner(0),
                Kroner(0)
            ),
            vedleggInnhold = emptyList(),
            utbetaltBeloep = Kroner(4444),
            rettsgebyrBeloep = Kroner(1234),
            harOpphoer = false,
            mottattSkatteoppgjoer = false
        )
    )

fun createEtteroppgjoerVedtakRedigerbartUtfallBrevDTO() =
    EtteroppgjoerVedtakRedigerbartUtfallBrevDTO(
        data = EtteroppgjoerVedtakRedigerbartUtfallInnholdDTO(
            etteroppgjoersAar = 2024,
            forhaandsvarselSendtDato = null,
            mottattSvarDato = LocalDate.of(2024, 7,15)
        )
    )