package no.nav.pensjon.brev.api.model.maler.ufoerApi

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.ufoerApi.AvslagUforetrygdDto.PenData
import no.nav.pensjon.brevbaker.api.model.DisplayText

data class AvslagUforetrygdDto(
    override val pesysData: PenData,
    override val saksbehandlerValg: Saksbehandlervalg
) : RedigerbarBrevdata<AvslagUforetrygdDto.Saksbehandlervalg, PenData> {

    data class Saksbehandlervalg(
        @DisplayText("Vurdert ung ufør")
        val vurdertUngUfor: Boolean,
    ) : BrevbakerBrevdata

    data class PenData (
        val placeholder: String
    ) : BrevbakerBrevdata
}