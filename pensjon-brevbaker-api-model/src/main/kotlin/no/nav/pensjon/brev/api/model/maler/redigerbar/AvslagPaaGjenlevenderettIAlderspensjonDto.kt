package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.KravInitiertAv
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner

@Suppress("unused")
data class AvslagPaaGjenlevenderettIAlderspensjonDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<AvslagPaaGjenlevenderettIAlderspensjonDto.SaksbehandlerValg, AvslagPaaGjenlevenderettIAlderspensjonDto.PesysData> {
    data class SaksbehandlerValg(val samboerUtenFellesBarn: Boolean) : BrevbakerBrevdata
    data class PesysData(
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val krav: Krav,
        val avdoed: Avdoed
    ) : BrevbakerBrevdata {
        data class AlderspensjonVedVirk(val totalPensjon: Kroner)
        data class Krav(val kravInitiertAv: KravInitiertAv)
        data class Avdoed(val navn: String)
    }
}