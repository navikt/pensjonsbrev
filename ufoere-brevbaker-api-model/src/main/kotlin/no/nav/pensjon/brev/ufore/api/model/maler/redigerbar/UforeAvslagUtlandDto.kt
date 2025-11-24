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
        @DisplayText("Bruk vurdering fra vilkårsvedtak")
        val VisVurderingFraVilkarvedtak: Boolean,
        @DisplayText("Innvilget pensjon fra EØS-land")
        val visInnvilgetPensjonEOSLand: Boolean,

    ) : SaksbehandlerValgBrevdata

    data class UforeAvslagPendata(
        val kravMottattDato: LocalDate,
        val vurdering: String,
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
