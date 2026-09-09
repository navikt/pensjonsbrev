package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import java.time.LocalDate

data class UforeAvslagTestmalDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: UforeAvslagPendata,
) : RedigerbarBrevdata<UforeAvslagTestmalDto.UforeAvslagPendata> {

    data class UforeAvslagPendata(
        val kravMottattDato: LocalDate,
        val vurdering: List<String>,
        val vurderingsTekst: String,
    ) : FagsystemBrevdata
}
