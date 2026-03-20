package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonUtlandDto

fun createAvslagGjenlevendepensjonUtlandDto() =
    AvslagGjenlevendepensjonUtlandDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = EmptyFagsystemdata
    )