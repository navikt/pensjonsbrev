package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.PaaminnelseLeveattestDto
import java.time.LocalDate
import java.time.Month

fun createPaaminnelseLeveattestDto() = PaaminnelseLeveattestDto(
    saksbehandlerValg = EmptyBrevdata,
    pesysData = PaaminnelseLeveattestDto.Pesysdata(fristdato = LocalDate.of(2024, Month.OCTOBER, 29))
)