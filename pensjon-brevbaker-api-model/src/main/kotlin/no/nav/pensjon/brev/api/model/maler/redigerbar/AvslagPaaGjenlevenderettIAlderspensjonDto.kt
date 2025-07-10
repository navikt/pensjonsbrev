package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.KravInitiertAv
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Percent

@Suppress("unused")
data class AvslagPaaGjenlevenderettIAlderspensjonDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<AvslagPaaGjenlevenderettIAlderspensjonDto.SaksbehandlerValg, AvslagPaaGjenlevenderettIAlderspensjonDto.PesysData> {
    data class SaksbehandlerValg(val samboerUtenFellesBarn: Boolean) : BrevbakerBrevdata
    data class PesysData(
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val krav: Krav,
        val avdoed: Avdoed,
        val ytelseskomponentInformasjon: YtelseskomponentInformasjon,
        val beregnetPensjonPerManed: BeregnetPensjonPerManed
    ) : BrevbakerBrevdata {
        data class AlderspensjonVedVirk(val totalPensjon: Kroner, val uttaksgrad: Percent)
        data class Krav(val kravInitiertAv: KravInitiertAv)
        data class Avdoed(val navn: String)
        data class YtelseskomponentInformasjon(val beloepEndring: BeloepEndring)
        data class BeregnetPensjonPerManed(val antallBeregningsperioderPensjon: Int)
    }
}