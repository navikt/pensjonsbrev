package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.redigerbar.KlageOrienteringOmSaksbehandlingstidDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Foedselsnummer

fun createKlageOrienteringOmSaksbehandlingstidDto() =
    KlageOrienteringOmSaksbehandlingstidDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = KlageOrienteringOmSaksbehandlingstidDto.PesysData(
            foedselsnummer = Foedselsnummer("01010112345"),
            navn = "Per Pensjon",
            navAvsenderEnhet = "NAV Oslo"
        )
    )