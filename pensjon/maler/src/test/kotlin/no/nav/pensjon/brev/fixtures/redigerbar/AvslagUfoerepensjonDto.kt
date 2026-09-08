package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagUfoerepensjonDto
import java.time.LocalDate

fun createAvslagUfoerepensjonDto() =
    AvslagUfoerepensjonDto(
        kravMottattDato = LocalDate.of(2024, 1, 1)
    )