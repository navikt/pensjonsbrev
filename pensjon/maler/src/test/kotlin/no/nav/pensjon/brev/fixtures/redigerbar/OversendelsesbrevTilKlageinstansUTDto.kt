package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.OversendelsesbrevTilKlageinstansUTDto

fun createOversendelsesbrevTilKlageinstansDto() =
    OversendelsesbrevTilKlageinstansUTDto(
        saksbehandlerValg = lagSaksbehandlervalg(
            "trygdetid" to true
        ),
        pesysData = EmptyFagsystemdata
    )