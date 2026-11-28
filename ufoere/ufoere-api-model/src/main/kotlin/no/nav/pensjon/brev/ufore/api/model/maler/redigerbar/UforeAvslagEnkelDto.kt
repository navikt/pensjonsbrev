package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.ufore.api.model.maler.felles.Kravtype
import java.time.LocalDate

data class UforeAvslagEnkelDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: UforeAvslagPendata,
) : RedigerbarBrevdata<UforeAvslagEnkelDto.UforeAvslagPendata> {

    data class UforeAvslagPendata(
        val kravMottattDato: LocalDate,
        val kravtype: Kravtype,
        val vurdering: String,
        val bosattUtland: Boolean = false
    ) : FagsystemBrevdata
}
