package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.maler.RedigerbarBrevdata

data class OmregningAlderUfore2016RedigerbarDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: OmregningAlderUfore2016Dto
) : RedigerbarBrevdata<EmptySaksbehandlerValg, OmregningAlderUfore2016Dto>