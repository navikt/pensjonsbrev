package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagHensiktsmessigBehandlingDto.*
import no.nav.pensjon.brevbaker.api.model.DisplayText

data class UforeAvslagHensiktsmessigBehandlingDto(
    override val pesysData: UforeAvslagHensiktsmessigBehandlingPendata,
    override val saksbehandlerValg: Saksbehandlervalg
) : RedigerbarBrevdata<Saksbehandlervalg, UforeAvslagHensiktsmessigBehandlingPendata> {

    data class Saksbehandlervalg(
        @DisplayText("Bruk vurdering fra vilkårsvedtak")
        val brukVurderingFraVilkarsvedtak: Boolean,
    ) : BrevbakerBrevdata

    data class UforeAvslagHensiktsmessigBehandlingPendata(
        val vurdering: String
    ) : BrevbakerBrevdata
}
