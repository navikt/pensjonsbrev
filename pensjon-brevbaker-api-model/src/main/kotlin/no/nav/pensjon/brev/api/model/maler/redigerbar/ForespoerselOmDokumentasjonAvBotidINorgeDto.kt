package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

data class ForespoerselOmDokumentasjonAvBotidINorgeDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: EmptyBrevdata,
) : RedigerbarBrevdata<ForespoerselOmDokumentasjonAvBotidINorgeDto.SaksbehandlerValg, EmptyBrevdata> {
    data class SaksbehandlerValg(val opplystOmBotid: Boolean) : BrevbakerBrevdata
}
