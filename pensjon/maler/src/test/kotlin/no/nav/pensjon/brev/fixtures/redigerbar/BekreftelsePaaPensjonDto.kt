package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaPensjonDto
import java.time.LocalDate

fun createBekreftelsePaaPensjonDto() = BekreftelsePaaPensjonDto(
    saksbehandlerValg = lagSaksbehandlervalg(),
    pesysData = BekreftelsePaaPensjonDto.PesysData(
        foedselsdato = LocalDate.of(1950, 1, 1),
        navn = "Per Pensjon",
    )
)