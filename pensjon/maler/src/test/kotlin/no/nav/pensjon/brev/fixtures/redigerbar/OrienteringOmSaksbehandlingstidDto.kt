package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.OrienteringOmSaksbehandlingstidDto

fun createOrienteringOmSaksbehandlingstidDto() = OrienteringOmSaksbehandlingstidDto(
    saksbehandlerValg = lagSaksbehandlervalg("soeknadOversendesTilUtlandet" to false),
    pesysData = EmptyFagsystemdata
)