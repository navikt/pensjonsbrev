package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

@Suppress("unused")
data class VarselTilbakekrevingAvFeilutbetaltBeloepDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : RedigerbarBrevdataMedSaksbehandlerValg<VarselTilbakekrevingAvFeilutbetaltBeloepDto.PesysData> {

    data class PesysData(val sakstype: Sakstype) : FagsystemBrevdata
}
