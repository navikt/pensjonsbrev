package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonDto
import java.time.LocalDate

fun createAvslagGjenlevendepensjonDto() =
    AvslagGjenlevendepensjonDto(
        saksbehandlerValg = AvslagGjenlevendepensjonDto.SaksbehandlerValg(
            folketrygdlovenParagraf = AvslagGjenlevendepensjonDto.SaksbehandlerValg.FolketrygdlovenParagraf.paragraf17_2_foersteEllerTredje_ledd
        ),
        pesysData = AvslagGjenlevendepensjonDto.PesysData(
            kravMottattDato = LocalDate.of(2024, 1, 1),
        )
    )
