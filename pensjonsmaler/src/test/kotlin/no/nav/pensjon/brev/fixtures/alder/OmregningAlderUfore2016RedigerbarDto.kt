package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016RedigerbarDto

fun createOmregningAlderUfore2016RedigerbarDto() =
    OmregningAlderUfore2016RedigerbarDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = createOmregningAlderUfore2016Dto()
    )