package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brevbaker.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brevbaker.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText

@Suppress("unused")
data class VarselTilbakekrevingAvFeilutbetaltBeloepDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VarselTilbakekrevingAvFeilutbetaltBeloepDto.SaksbehandlerValg, VarselTilbakekrevingAvFeilutbetaltBeloepDto.PesysData> {

    data class SaksbehandlerValg(
        @DisplayText("Hvis aktuelt å ilegge rentetillegg")
        val hvisAktueltAaIleggeRentetillegg: Boolean
    ) : SaksbehandlerValgBrevdata

    data class PesysData(val sakstype: Sakstype) : FagsystemBrevdata
}
