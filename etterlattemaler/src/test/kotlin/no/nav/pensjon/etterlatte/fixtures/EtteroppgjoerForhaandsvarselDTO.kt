package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselBrevDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselInnholfDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselRedigerbartBrevDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerResultatType
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerBeregningVedleggInnholdDTO
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerGrunnlagDTO
import java.time.YearMonth

fun createEtteroppgjoerForhaandsvarselBrevDTO() =
    EtteroppgjoerForhaandsvarselBrevDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        data = EtteroppgjoerForhaandsvarselDTO(
            bosattUtland = true,
            norskInntekt = true,
            etteroppgjoersAar = 2024,
            rettsgebyrBeloep = Kroner(1234),
            resultatType = EtteroppgjoerResultatType.ETTERBETALING,
            stoenad = Kroner(321),
            faktiskStoenad = Kroner(4444),
            avviksBeloep = Kroner(20000),
            grunnlag = EtteroppgjoerGrunnlagDTO(
                YearMonth.of(2024,1),
                YearMonth.of(2024,12),
                12,
                Kroner(0),
                Kroner(0),
                Kroner(0),
                Kroner(0),
                Kroner(0),
                Kroner(0)
            ),
            vedleggInnhold = emptyList()
        ),
    )

fun createEtteroppgjoerForhaandsvarselRedigerbartBrevDTO() =
    EtteroppgjoerForhaandsvarselRedigerbartBrevDTO(
        data = EtteroppgjoerForhaandsvarselInnholfDTO(
            bosattUtland = true,
            norskInntekt = true,
            etteroppgjoersAar = 2024,
            rettsgebyrBeloep = Kroner(1234),
            resultatType = EtteroppgjoerResultatType.ETTERBETALING,
            avviksBeloep = Kroner(20000),
        )
    )

fun createEtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTO() =
    EtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTO(
        data = EtteroppgjoerBeregningVedleggInnholdDTO(
            etteroppgjoersAar = 2024,
            erVedtak = false,
        )
    )