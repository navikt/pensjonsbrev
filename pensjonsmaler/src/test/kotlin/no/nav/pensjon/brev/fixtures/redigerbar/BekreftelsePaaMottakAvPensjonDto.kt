package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaMottakAvPensjonDto
import java.time.LocalDate

fun createBekreftelsePaaMottakAvPensjonDto() = BekreftelsePaaMottakAvPensjonDto(
    saksbehandlerValg = EmptySaksbehandlerValg,
    pesysData = BekreftelsePaaMottakAvPensjonDto.PesysData(
        foedselsdato = LocalDate.of(1950, 1, 1),
        navn = "Per Pensjon",
    )
)