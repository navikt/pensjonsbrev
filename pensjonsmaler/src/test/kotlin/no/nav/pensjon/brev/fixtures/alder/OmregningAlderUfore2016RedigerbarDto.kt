package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016RedigerbarDto

fun createOmregningAlderUfore2016RedigerbarDto() =
    OmregningAlderUfore2016RedigerbarDto(
        saksbehandlerValg = EmptyBrevdata,
        pesysData = createOmregningAlderUfore2016Dto()
    )