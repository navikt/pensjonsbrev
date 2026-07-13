package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.redigerbar.KlageOrienteringOmOversendelseTilKlageinstansDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Foedselsnummer

fun createKlageOrienteringOmOversendelseTilKlageinstansDto() =
    KlageOrienteringOmOversendelseTilKlageinstansDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = KlageOrienteringOmOversendelseTilKlageinstansDto.PesysData(
            foedselsnummer = Foedselsnummer("01010112345"),
            navn = "Per Pensjon",
            navnAvsenderEnhet = "NAV Oslo"
        )
    )