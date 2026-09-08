package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonDto
import java.time.LocalDate

fun createAvslagGjenlevendepensjonDto() =
    AvslagGjenlevendepensjonDto(
        kravMottattDato = LocalDate.of(2024, 1, 1),
    )
