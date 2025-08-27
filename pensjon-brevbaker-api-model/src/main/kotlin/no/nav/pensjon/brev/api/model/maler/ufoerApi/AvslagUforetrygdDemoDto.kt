package no.nav.pensjon.brev.api.model.maler.ufoerApi

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.ufoerApi.AvslagUforetrygdDemoDto.PenData
import no.nav.pensjon.brevbaker.api.model.DisplayText

data class AvslagUforetrygdDemoDto(
    override val pesysData: PenData,
    override val saksbehandlerValg: Saksbehandlervalg
) : RedigerbarBrevdata<AvslagUforetrygdDemoDto.Saksbehandlervalg, PenData> {

    data class Saksbehandlervalg(
        @DisplayText("Vurdert ung ufør")
        val vurdertUngUfor: Boolean,
    ) : BrevbakerBrevdata

    data class PenData (
        val placeholder: String
    ) : BrevbakerBrevdata
}