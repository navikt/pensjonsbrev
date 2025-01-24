package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

@Suppress("unused")
data class InformasjonOmSaksbehandlingstidUtDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: EmptyBrevdata
) : RedigerbarBrevdata<InformasjonOmSaksbehandlingstidUtDto.SaksbehandlerValg, EmptyBrevdata> {
    data class SaksbehandlerValg(
        val soeknadMottattFraUtland: Boolean = false,
        val venterPaaSvarAFP: Boolean = false,
    ) : BrevbakerBrevdata
}
