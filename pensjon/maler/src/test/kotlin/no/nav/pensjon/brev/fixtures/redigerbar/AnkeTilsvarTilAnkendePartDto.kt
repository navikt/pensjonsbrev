package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.redigerbar.AnkeTilsvarTilAnkendePartDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Foedselsnummer

fun createAnkeTilsvarTilAnkendePartDto() =
    AnkeTilsvarTilAnkendePartDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = AnkeTilsvarTilAnkendePartDto.PesysData(
            foedselsnummer = Foedselsnummer("01010112345"),
            navn = "Per Pensjon",
            navnAvsenderEnhet = "NAV Oslo"
        )
    )