package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAndreAvvikEtterSvarDto
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

fun createVedtakAfpEtteroppgjoerIngenEndringAndreAvvikEtterSvarDto(): VedtakAfpEtteroppgjoerIngenEndringAndreAvvikEtterSvarDto =
    VedtakAfpEtteroppgjoerIngenEndringAndreAvvikEtterSvarDto(EmptySaksbehandlerValg,
        VedtakAfpEtteroppgjoerIngenEndringAndreAvvikEtterSvarDto.PesysData(
            oppgjoersAar = Year(2024),
            inntektFoerUttak = Kroner(412_500),
            scenario = VedtakAfpEtteroppgjoerIngenEndringAndreAvvikEtterSvarDto.Scenario.HEL_AFP_INNTEKT_INNEN_TOLERANSE,
            medlemAvApotekerordningen = false,
            toleranseBeloep = Kroner(15001)
        )
    )
