package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

fun createVedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDto(): VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDto =
    VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDto(
        oppgjoersAar = Year(2024),
        pensjonsgivendeInntekt = Kroner(412_500),
        inntektFoerUttak = Kroner(85_000),
        inntektEtterOpphoer = Kroner(120_000),
        inntektIAfpPerioden = Kroner(207_500),
        forventetPensjonsgivendeInntektBeregnet = Kroner(210_000),
        avvik = Kroner(12_500),
        scenario = VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDto.Scenario.IFU_OG_IEO_REGISTRERT,
        medlemAvApotekerordningen = false,
        toleranseBeloep = Kroner(15001),
    )
