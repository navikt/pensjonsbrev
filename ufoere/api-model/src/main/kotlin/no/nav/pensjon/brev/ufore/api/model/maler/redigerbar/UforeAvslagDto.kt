package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import java.time.LocalDate

data class UforeAvslagDto(
    override val pesysData: UforeAvslagPendata,
    override val saksbehandlerValg: EmptySaksbehandlerValg
) : RedigerbarBrevdata<EmptySaksbehandlerValg, UforeAvslagDto.UforeAvslagPendata> {

    data class UforeAvslagPendata(
        val kravMottattDato: LocalDate,
        val vurdering: String
    ) : FagsystemBrevdata
}
