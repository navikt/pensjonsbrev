package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar

import no.nav.pensjon.brevbaker.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brevbaker.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUforetidspunkt26Dto.Saksbehandlervalg
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUforetidspunkt26Dto.UforeAvslagPendata
import no.nav.pensjon.brevbaker.api.model.DisplayText
import java.time.LocalDate

data class UforeAvslagUforetidspunkt26Dto(
    override val pesysData: UforeAvslagPendata,
    override val saksbehandlerValg: Saksbehandlervalg
) : RedigerbarBrevdata<Saksbehandlervalg, UforeAvslagPendata> {

    data class Saksbehandlervalg(
        @DisplayText("Bruk vurdering fra vilkårsvedtak")
        val VisVurderingFraVilkarvedtak: Boolean,
        @DisplayText("Uføretidspunkt etter 26 år")
        val visUforetidspunktEtter26: Boolean,
    ) : SaksbehandlerValgBrevdata

    data class UforeAvslagPendata(
        val kravMottattDato: LocalDate,
        val vurdering: String
    ) : FagsystemBrevdata
}
