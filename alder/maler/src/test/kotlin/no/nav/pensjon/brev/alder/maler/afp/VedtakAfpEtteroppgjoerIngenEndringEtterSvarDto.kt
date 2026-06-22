package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarDto
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

fun createVedtakAfpEtteroppgjoerIngenEndringEtterSvarDto(): VedtakAfpEtteroppgjoerIngenEndringEtterSvarDto =
    VedtakAfpEtteroppgjoerIngenEndringEtterSvarDto(
        EmptySaksbehandlerValg,
        VedtakAfpEtteroppgjoerIngenEndringEtterSvarDto.PesysData(
            oppgjoersAar = Year(2024),
            pensjonsgivendeInntekt = Kroner(412_500),
            inntektFoerUttak = Kroner(85_000),
            inntektEtterOpphoer = Kroner(120_000),
            inntektIAfpPerioden = Kroner(207_500),
            forventetPensjonsgivendeInntektBeregnet = Kroner(210_000),
            avvik = Kroner(12_500),
            scenario = VedtakAfpEtteroppgjoerIngenEndringEtterSvarDto.Scenario.IFU_OG_IEO_REGISTRERT,
            medlemAvApotekerordningen = false,
            toleranseBeloep = Kroner(15001)
        )
    )
