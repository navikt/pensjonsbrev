package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.VedleggBrevdata

@Suppress("unused")
data class VarselTilbakekrevingAvFeilutbetaltBeloepDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VarselTilbakekrevingAvFeilutbetaltBeloepDto.SaksbehandlerValg, VarselTilbakekrevingAvFeilutbetaltBeloepDto.PesysData>, VedleggBrevdata {

    data class SaksbehandlerValg(
        val hvisAktueltAaIleggeRentetillegg: Boolean,
        val hvisGjenlevendepensjonOgForsoergningstillegg: Boolean
    ) : BrevbakerBrevdata

    data class PesysData(val sakstype: Sakstype) : BrevbakerBrevdata
}
