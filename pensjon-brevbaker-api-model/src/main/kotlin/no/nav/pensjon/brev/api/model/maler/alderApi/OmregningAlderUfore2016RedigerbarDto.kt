package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

data class OmregningAlderUfore2016RedigerbarDto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: OmregningAlderUfore2016Dto
) : RedigerbarBrevdata<EmptyBrevdata, OmregningAlderUfore2016Dto>