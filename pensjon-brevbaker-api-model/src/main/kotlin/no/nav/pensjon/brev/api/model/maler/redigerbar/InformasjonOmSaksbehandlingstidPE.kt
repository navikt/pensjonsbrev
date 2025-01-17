package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

@Suppress("unused")
data class InformasjonOmSaksbehandlingstidPeDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: EmptyBrevdata
) : RedigerbarBrevdata<InformasjonOmSaksbehandlingstidPeDto.SaksbehandlerValg, EmptyBrevdata> {
    data class SaksbehandlerValg(
        val soeknadMottattFraUtland: Boolean = false,
        val venterPaaSvarAFP: Boolean = false,
        val forlengetSaksbehandling: Boolean = false,
    ) : BrevbakerBrevdata
}