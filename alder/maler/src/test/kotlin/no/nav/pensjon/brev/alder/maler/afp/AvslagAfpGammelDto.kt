package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.AvslagAfpGammelDto
import no.nav.brev.brevbaker.lagSaksbehandlervalg
import java.time.LocalDate

fun createAvslagAfpGammelDto(): AvslagAfpGammelDto =
    AvslagAfpGammelDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = AvslagAfpGammelDto.PesysData(
            kravMottattDato = LocalDate.of(2012, 10, 5),
        ),
    )
