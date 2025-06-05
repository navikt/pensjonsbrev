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
            bosattUtland = false,
            norskInntekt = true,
            etteroppgjoersAar = 2024,
            rettsgebyrBeloep = Kroner(1234),
            resultatType = EtteroppgjoerResultatType.IKKE_ETTEROPPGJOER,
            stoenad = Kroner(321),
            faktiskStoenad = Kroner(4444),
            avviksBeloep = Kroner(0),
            grunnlag = EtteroppgjoerGrunnlagDTO(
                YearMonth.of(2024,1),
                YearMonth.of(2024,12),
                12,
                Kroner(4),
                Kroner(40),
                Kroner(400),
                Kroner(4000),
                Kroner(4444)
            ),
            vedleggInnhold = emptyList()
        ),
    )

fun createEtteroppgjoerForhaandsvarselRedigerbartBrevDTO() =
    EtteroppgjoerForhaandsvarselRedigerbartBrevDTO(
        data = EtteroppgjoerForhaandsvarselInnholfDTO(
            bosattUtland = false,
            norskInntekt = true,
            etteroppgjoersAar = 2024,
            rettsgebyrBeloep = Kroner(1234),
            resultatType = EtteroppgjoerResultatType.IKKE_ETTEROPPGJOER,
            avviksBeloep = Kroner(0),
        )
    )

fun createEtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTO() =
    EtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTO(
        data = EtteroppgjoerBeregningVedleggInnholdDTO(
            etteroppgjoersAar = 2024,
        )
    )