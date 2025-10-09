package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText

data class OrienteringOmSaksbehandlingstidDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: EmptyBrevdata,
) : RedigerbarBrevdata<OrienteringOmSaksbehandlingstidDto.SaksbehandlerValg, EmptyBrevdata> {
    data class SaksbehandlerValg(
        @DisplayText("Søknad oversendes til utlandet")
        val soeknadOversendesTilUtlandet: Boolean
    ) : SaksbehandlerValgBrevdata
}