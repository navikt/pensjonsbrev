package no.nav.pensjon.brev.alder.model.aldersovergang.omregning

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

data class OmregningAlderUfore2016RedigerbarDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: OmregningAlderUfore2016Dto,
) : RedigerbarBrevdata<OmregningAlderUfore2016Dto>