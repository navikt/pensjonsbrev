package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUforetidspunkt26Dto.UforeAvslagPendata
import java.time.LocalDate

data class UforeAvslagUforetidspunkt26Dto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: UforeAvslagPendata,
) : RedigerbarBrevdataMedSaksbehandlerValg<UforeAvslagPendata> {

    data class UforeAvslagPendata(
        val kravMottattDato: LocalDate,
        val vurdering: String
    ) : FagsystemBrevdata
}
