package no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText

data class UforeTrygdSokerAfpPrivatDto(
    override val saksbehandlerValg : SaksBehandlerValg,
    override val pesysData: EmptyFagsystemdata,


    ) : RedigerbarBrevdata<UforeTrygdSokerAfpPrivatDto.SaksBehandlerValg, EmptyFagsystemdata> {
    data class SaksBehandlerValg(
        @DisplayText("Bruker har søkt AfpPrivat")
        val brukerHarSoktAfpPrivat: Boolean,
    ) : SaksbehandlerValgBrevdata
}


