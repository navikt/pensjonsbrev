package no.nav.pensjon.etterlatte.fixtures.vedlegg

import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerUtbetalingDTO
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.BeregningsVedleggData
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerGrunnlagDTO
import java.time.YearMonth

fun lagBeregningsVedleggData() = BeregningsVedleggData(
    innhold = listOf(),
    etteroppgjoersAar = 2025,
    utbetalingData = EtteroppgjoerUtbetalingDTO(
        stoenadUtbetalt = Kroner(1500),
        faktiskStoenad = Kroner(2000),
        avviksBeloep = Kroner(500)
    ),
    grunnlag = EtteroppgjoerGrunnlagDTO(
        fom = YearMonth.now(),
        tom = YearMonth.now(),
        innvilgedeMaaneder = 1,
        loennsinntekt = Kroner(200),
        naeringsinntekt = Kroner(300),
        afp = Kroner(50),
        utlandsinntekt = Kroner(100),
        inntekt = Kroner(700),
        pensjonsgivendeInntektHeleAaret = Kroner(2000)
    ),
    erVedtak = false,
    mottattSkatteoppgjoer = true
)