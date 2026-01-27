package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText

@Suppress("unused")
data class InformasjonOmSaksbehandlingstidUtDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: EmptyFagsystemdata
) : RedigerbarBrevdata<InformasjonOmSaksbehandlingstidUtDto.SaksbehandlerValg, EmptyFagsystemdata> {
    data class SaksbehandlerValg(
        @DisplayText("Forlenget saksbehandlingstid")
        val forlengetSaksbehandlingstid: Boolean = false,
    ) : SaksbehandlerValgBrevdata
}
