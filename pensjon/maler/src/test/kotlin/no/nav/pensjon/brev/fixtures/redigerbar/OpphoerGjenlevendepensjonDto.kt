package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.OpphoerGjenlevendepensjonDto

fun createOpphoerGjenlevendepensjonDto() =
    OpphoerGjenlevendepensjonDto(
        saksbehandlerValg = OpphoerGjenlevendepensjonDto.SaksbehandlerValg(
            folketrygdlovenAlternativ = OpphoerGjenlevendepensjonDto.SaksbehandlerValg.FolketrygdlovenAlternativ.gifterSeg,
            opphoerMedTilbakekreving = false,
        ),
        pesysData = EmptyFagsystemdata,
    )
