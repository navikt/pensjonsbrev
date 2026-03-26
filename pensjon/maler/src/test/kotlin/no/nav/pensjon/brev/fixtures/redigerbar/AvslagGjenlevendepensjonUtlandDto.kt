package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonUtlandDto
import java.time.LocalDate

fun createAvslagGjenlevendepensjonUtlandDto() =
    AvslagGjenlevendepensjonUtlandDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = AvslagGjenlevendepensjonUtlandDto.PesysData(
            kravMottattDato = LocalDate.of(2024, 1, 1)
        )
    )