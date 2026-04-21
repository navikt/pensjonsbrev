package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagUfoerepensjonDto
import java.time.LocalDate

fun createAvslagUfoerepensjonDto() =
    AvslagUfoerepensjonDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = AvslagUfoerepensjonDto.PesysData(
            kravMottattDato = LocalDate.of(2024, 1, 1)
        )
    )