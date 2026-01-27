package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText

data class OrienteringOmSaksbehandlingstidDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: EmptyFagsystemdata,
) : RedigerbarBrevdata<OrienteringOmSaksbehandlingstidDto.SaksbehandlerValg, EmptyFagsystemdata> {
    data class SaksbehandlerValg(
        @DisplayText("Søknad oversendes til utlandet")
        val soeknadOversendesTilUtlandet: Boolean
    ) : SaksbehandlerValgBrevdata
}