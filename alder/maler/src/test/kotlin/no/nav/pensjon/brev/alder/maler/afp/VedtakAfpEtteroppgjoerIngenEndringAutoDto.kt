package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAutoDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

fun createVedtakAfpEtteroppgjoerIngenEndringAutoDto(): VedtakAfpEtteroppgjoerIngenEndringAutoDto =
    VedtakAfpEtteroppgjoerIngenEndringAutoDto(
        oppgjoersAar = Year(2024),
        pgi = Kroner(412_500),
        scenario = VedtakAfpEtteroppgjoerIngenEndringAutoDto.Scenario.IKKE_AFP_FULL_INNTEKT,
    )
