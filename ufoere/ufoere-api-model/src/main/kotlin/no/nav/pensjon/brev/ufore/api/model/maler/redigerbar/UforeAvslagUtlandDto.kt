package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import java.time.LocalDate

data class UforeAvslagUtlandDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: UforeAvslagPendata,
) : RedigerbarBrevdataMedSaksbehandlerValg<UforeAvslagUtlandDto.UforeAvslagPendata> {

    data class UforeAvslagPendata(
        val kravMottattDato: LocalDate,
        val kravGjelder: KravGjelder,
        val eosNordisk: Boolean = false,
        val avtaletype: String? = null,
        val artikkel: String? = null,
        val trygdetidListe: List<Trygdetid>
    ) : FagsystemBrevdata

    enum class KravGjelder {
        MELLOMBH,
        F_BH_BO_UTL,
        SLUTT_BH_UTL
    }

    data class Trygdetid(
        val land: String,
        val fomDato: LocalDate,
        val tomDato: LocalDate,
    )
}
