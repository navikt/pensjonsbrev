package no.nav.pensjon.brev.alder.maler.aldersovergang.omregning

import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016RedigerbarDto
import no.nav.brev.brevbaker.lagSaksbehandlervalg

fun createOmregningAlderUfore2016RedigerbarDto() =
    OmregningAlderUfore2016RedigerbarDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = createOmregningAlderUfore2016Dto()
    )