package no.nav.pensjon.brev.alder.maler.aldersovergang.omregning

import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016RedigerbarDto
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg

fun createOmregningAlderUfore2016RedigerbarDto() =
    OmregningAlderUfore2016RedigerbarDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = createOmregningAlderUfore2016Dto()
    )