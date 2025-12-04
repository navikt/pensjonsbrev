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
        @DisplayText("Tekst hvis bruker ikke omfattes av personkretsen i trygdeforordningen")
        val visBrukerIkkeOmfattesAvPersonkretsTrygdeforordning: Boolean = false,

        @DisplayText("Tekst ved artikkel 57 avslag")
        val visTekstVedArtikkel57Avslag: Boolean = false,

        @DisplayText("Bruker har fått innvilget pensjon fra EØS-land")
        val visInnvilgetPensjonEOSLand: Boolean = false,

        @DisplayText("Vedtak fra andre land")
        val visVedtakFraAndreLand: Boolean = false,

    ) : SaksbehandlerValgBrevdata

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
        F_BH_BO_UTL
    }

    data class Trygdetid(
        val land: String,
        val fomDato: LocalDate,
        val tomDato: LocalDate,
    )
}
