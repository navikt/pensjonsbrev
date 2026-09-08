package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.AvslagAfpGammelDto
import java.time.LocalDate

fun createAvslagAfpGammelDto(): AvslagAfpGammelDto =
    AvslagAfpGammelDto(
        kravMottattDato = LocalDate.of(2012, 10, 5),
    )
