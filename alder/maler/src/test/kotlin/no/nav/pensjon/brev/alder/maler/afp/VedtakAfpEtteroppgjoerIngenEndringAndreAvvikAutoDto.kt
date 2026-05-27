package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikAutoDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

fun createVedtakAfpEtteroppgjoerIngenEndringAndreAvvikAutoDto(): VedtakAfpEtteroppgjoerIngenEndringAndreAvvikAutoDto =
    VedtakAfpEtteroppgjoerIngenEndringAndreAvvikAutoDto(
        oppgjoersAar = Year(2024),
        pensjonsgivendeInntekt = Kroner(412_500),
        scenario = VedtakAfpEtteroppgjoerIngenEndringAndreAvvikAutoDto.Scenario.IKKE_AFP_FULL_INNTEKT,
    )
