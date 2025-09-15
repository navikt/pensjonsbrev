package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText

data class AvslagUforetrygdDemoDto(
    override val pesysData: EmptyRedigerbarBrevdata = EmptyRedigerbarBrevdata,
    override val saksbehandlerValg: Saksbehandlervalg
) : RedigerbarBrevdata<AvslagUforetrygdDemoDto.Saksbehandlervalg, EmptyRedigerbarBrevdata> {

    data class Saksbehandlervalg(
        @DisplayText("Vurdert ung ufør")
        val vurdertUngUfor: Boolean,
    ) : BrevbakerBrevdata
}