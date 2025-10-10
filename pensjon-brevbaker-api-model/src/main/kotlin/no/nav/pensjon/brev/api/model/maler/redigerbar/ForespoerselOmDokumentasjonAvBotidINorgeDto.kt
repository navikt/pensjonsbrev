package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata

data class ForespoerselOmDokumentasjonAvBotidINorgeDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: EmptyBrevdata,
) : RedigerbarBrevdata<ForespoerselOmDokumentasjonAvBotidINorgeDto.SaksbehandlerValg, EmptyBrevdata> {
    data class SaksbehandlerValg(val opplystOmBotid: Boolean) : SaksbehandlerValgBrevdata
}
