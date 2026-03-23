package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonDto

fun createAvslagGjenlevendepensjonDto() =
    AvslagGjenlevendepensjonDto(
        saksbehandlerValg = AvslagGjenlevendepensjonDto.SaksbehandlerValg(
            folketrygdlovenParagraf = AvslagGjenlevendepensjonDto.SaksbehandlerValg.FolketrygdlovenParagraf.paragraf17_2_foersteEllerTredje_ledd
        ),
        pesysData = EmptyFagsystemdata,
    )
