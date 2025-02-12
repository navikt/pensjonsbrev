package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.OrienteringOmSaksbehandlingstidDto

fun createOrienteringOmSaksbehandlingstidDto() =
    OrienteringOmSaksbehandlingstidDto(
        saksbehandlerValg =
            OrienteringOmSaksbehandlingstidDto.SaksbehandlerValg(
                soeknadOversendesTilUtlandet = false,
            ),
        pesysData = EmptyBrevdata,
    )
