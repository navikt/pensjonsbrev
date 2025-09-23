package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText

@Suppress("unused")
data class VarselTilbakekrevingAvFeilutbetaltBeloepDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VarselTilbakekrevingAvFeilutbetaltBeloepDto.SaksbehandlerValg, VarselTilbakekrevingAvFeilutbetaltBeloepDto.PesysData> {

    data class SaksbehandlerValg(
        val hvisAktueltAaIleggeRentetillegg: Boolean,
        @DisplayText("Hvis avregning etter § 22-16")
        val hvisAvregningEtterParagraf2216: Boolean
    ) : BrevbakerBrevdata

    data class PesysData(val sakstype: Sakstype) : BrevbakerBrevdata
}
