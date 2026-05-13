package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEoFase2AutoDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

fun createVedtakAfpEtteroppgjoerEoFase2AutoDto(): VedtakAfpEtteroppgjoerEoFase2AutoDto =
    VedtakAfpEtteroppgjoerEoFase2AutoDto(
        oppgjoersAar = 2024,
        ifu = Kroner(412_500),
        scenario = VedtakAfpEtteroppgjoerEoFase2AutoDto.Scenario.HEL_AFP_INNTEKT_INNEN_TOLERANSE,
    )
