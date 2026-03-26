package no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText

data class AfpPrivatSokerUforeTrygdDto (
    override val saksbehandlerValg: SaksBehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<AfpPrivatSokerUforeTrygdDto.SaksBehandlerValg, AfpPrivatSokerUforeTrygdDto.PesysData> {

    data class SaksBehandlerValg(
        @DisplayText("Bruker har søkt UforeTrygd")
        val harSoktUforeTrygd: Boolean,
    ) : SaksbehandlerValgBrevdata

    data class PesysData(
        val uforeTrygdTil_ATT: Boolean
    ) : FagsystemBrevdata
}