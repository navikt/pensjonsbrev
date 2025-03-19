package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

@Suppress("unused")
data class VarselTilbakekrevingAvFeilutbetaltBeloepDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VarselTilbakekrevingAvFeilutbetaltBeloepDto.SaksbehandlerValg, VarselTilbakekrevingAvFeilutbetaltBeloepDto.PesysData> {
    data class SaksbehandlerValg(val hvisAktueltAaIleggeRentetillegg: Boolean) : BrevbakerBrevdata
    data class PesysData(val sakstype: Sakstype) : BrevbakerBrevdata
}
