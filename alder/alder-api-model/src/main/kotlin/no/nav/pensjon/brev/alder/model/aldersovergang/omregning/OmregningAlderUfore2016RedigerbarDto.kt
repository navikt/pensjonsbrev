package no.nav.pensjon.brev.alder.model.aldersovergang.omregning

import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

data class OmregningAlderUfore2016RedigerbarDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: OmregningAlderUfore2016Dto,
) : BrevdataMedSaksbehandlerValg<OmregningAlderUfore2016Dto>