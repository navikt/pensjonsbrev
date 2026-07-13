package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText
import java.time.LocalDate

data class UforeAvslagTestmalDto(
    override val pesysData: UforeAvslagPendata,
    override val saksbehandlerValg: Saksbehandlervalg
) : RedigerbarBrevdata<UforeAvslagTestmalDto.Saksbehandlervalg, UforeAvslagTestmalDto.UforeAvslagPendata> {

    data class Saksbehandlervalg(
        @DisplayText("Bruk vurdering fra vilkårsvedtak")
        val VisVurderingFraVilkarvedtak: Boolean,
    ) : SaksbehandlerValgBrevdata

    data class UforeAvslagPendata(
        val kravMottattDato: LocalDate,
        val vurdering: List<String>
    ) : FagsystemBrevdata
}
