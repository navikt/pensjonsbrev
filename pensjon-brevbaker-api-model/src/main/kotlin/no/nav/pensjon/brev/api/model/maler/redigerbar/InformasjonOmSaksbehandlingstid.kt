package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText

@Suppress("unused")
data class InformasjonOmSaksbehandlingstidDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: EmptyBrevdata,
) : RedigerbarBrevdata<InformasjonOmSaksbehandlingstidDto.SaksbehandlerValg, EmptyBrevdata> {
    data class SaksbehandlerValg(
        @DisplayText("Søknad mottatt fra utland")
        val soeknadMottattFraUtland: Boolean = false,
        @DisplayText("Venter på svar AFP")
        val venterPaaSvarAFP: Boolean = false,
    ) : SaksbehandlerValgBrevdata
}