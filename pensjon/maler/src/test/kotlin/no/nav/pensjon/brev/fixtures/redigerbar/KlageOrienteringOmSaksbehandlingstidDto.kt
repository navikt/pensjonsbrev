package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.KlageOrienteringOmSaksbehandlingstidDto

fun createKlageOrienteringOmSaksbehandlingstidDto() = KlageOrienteringOmSaksbehandlingstidDto(
    pesysData = EmptyFagsystemdata,
    saksbehandlerValg = lagSaksbehandlervalg()
)