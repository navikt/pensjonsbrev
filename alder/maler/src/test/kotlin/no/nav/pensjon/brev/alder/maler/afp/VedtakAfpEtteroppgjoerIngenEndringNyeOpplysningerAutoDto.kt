package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringNyeOpplysningerAutoDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

fun createVedtakAfpEtteroppgjoerIngenEndringNyeOpplysningerAutoDto(): VedtakAfpEtteroppgjoerIngenEndringNyeOpplysningerAutoDto =
    VedtakAfpEtteroppgjoerIngenEndringNyeOpplysningerAutoDto(
        oppgjoersAar = Year(2024),
        ifu = Kroner(412_500),
        scenario = VedtakAfpEtteroppgjoerIngenEndringNyeOpplysningerAutoDto.Scenario.HEL_AFP_INNTEKT_INNEN_TOLERANSE,
    )
