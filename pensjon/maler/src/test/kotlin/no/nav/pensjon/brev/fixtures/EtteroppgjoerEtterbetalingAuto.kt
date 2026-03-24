package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.EtteroppgjoerEtterbetalingAutoDto

fun createEtteroppgjoerEtterbetalingAuto() =
    EtteroppgjoerEtterbetalingAutoDto(
        pe = createPEgruppe10(),
        orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto()
    )