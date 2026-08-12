package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.OpphoerGjenlevendepensjonDto

fun createOpphoerGjenlevendepensjonDto() =
    OpphoerGjenlevendepensjonDto(
        saksbehandlerValg = lagSaksbehandlervalg(
            "folketrygdlovenAlternativ" to OpphoerGjenlevendepensjonDto.FolketrygdlovenAlternativ.gifterSeg,
            "opphoerMedTilbakekreving" to false,
        ),
        pesysData = EmptyFagsystemdata,
    )
