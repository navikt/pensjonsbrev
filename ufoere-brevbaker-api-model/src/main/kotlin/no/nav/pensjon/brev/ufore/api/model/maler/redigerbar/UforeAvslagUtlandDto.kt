package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText
import java.time.LocalDate

data class UforeAvslagUtlandDto(
    override val pesysData: UforeAvslagPendata,
    override val saksbehandlerValg: Saksbehandlervalg
) : RedigerbarBrevdata<UforeAvslagUtlandDto.Saksbehandlervalg, UforeAvslagUtlandDto.UforeAvslagPendata> {

    data class Saksbehandlervalg(
        @DisplayText("Bruker har fått innvilget pensjon fra EØS-land")
        val visInnvilgetPensjonEOSLand: Boolean = false,

        @DisplayText("Alternativ tekst hvis bruker ikke omfattes av personkretsen i trygdeforordningen")
        val visBrukerIkkeOmfattesAvPersonkretsTrygdeforordning: Boolean = false,

    ) : SaksbehandlerValgBrevdata

    data class UforeAvslagPendata(
        val kravMottattDato: LocalDate,
        val kravGjelder: KravGjelder,
        val trygdetidListe: List<Trygdetid>
    ) : FagsystemBrevdata

    enum class KravGjelder {
        MELLOMBH,
        F_BH_BO_UTL
    }

    data class Trygdetid(
        val land: String,
        val fomDato: LocalDate,
        val tomDato: LocalDate,
    )
}
