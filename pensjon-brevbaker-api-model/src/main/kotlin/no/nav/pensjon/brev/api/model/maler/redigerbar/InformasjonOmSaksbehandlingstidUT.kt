package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata

@Suppress("unused")
data class InformasjonOmSaksbehandlingstidUtDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: EmptyBrevdata
) : RedigerbarBrevdata<InformasjonOmSaksbehandlingstidUtDto.SaksbehandlerValg, EmptyBrevdata> {
    data class SaksbehandlerValg(
        val forlengetSaksbehandlingstid: Boolean = false,
    ) : SaksbehandlerValgBrevdata
}
