package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.ufore.api.model.maler.felles.Kravtype
import java.time.LocalDate

data class UforeAvslagUtlandDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: UforeAvslagPendata,
) : RedigerbarBrevdata<UforeAvslagUtlandDto.UforeAvslagPendata> {

    data class UforeAvslagPendata(
        val kravMottattDato: LocalDate,
        val kravtype: Kravtype,
        val eosNordisk: Boolean = false,
        val avtaletype: String? = null,
        val artikkel: String? = null,
        val trygdetidListe: List<Trygdetid>
    ) : FagsystemBrevdata

    data class Trygdetid(
        val land: String,
        val fomDato: LocalDate,
        val tomDato: LocalDate,
    )
}
