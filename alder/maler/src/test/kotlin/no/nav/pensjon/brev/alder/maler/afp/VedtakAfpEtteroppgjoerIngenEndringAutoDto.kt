package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAutoDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

fun createVedtakAfpEtteroppgjoerIngenEndringAutoDto(): VedtakAfpEtteroppgjoerIngenEndringAutoDto =
    VedtakAfpEtteroppgjoerIngenEndringAutoDto(
        oppgjoersAar = 2024,
        pgi = Kroner(412_500),
        scenario = VedtakAfpEtteroppgjoerIngenEndringAutoDto.Scenario.IKKE_AFP_FULL_INNTEKT,
    )
