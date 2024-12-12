package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.OrienteringOmSaksbehandlingstidDto
import java.time.LocalDate
import java.time.Month

fun createOrienteringOmSaksbehandlingstidDto() = OrienteringOmSaksbehandlingstidDto(
    saksbehandlerValg = OrienteringOmSaksbehandlingstidDto.SaksbehandlerValg(
        mottattSoeknad = LocalDate.of(2024, Month.OCTOBER, 28),
        soeknadOversendesTilUtlandet = false,
    ),
    pesysData = EmptyBrevdata
)