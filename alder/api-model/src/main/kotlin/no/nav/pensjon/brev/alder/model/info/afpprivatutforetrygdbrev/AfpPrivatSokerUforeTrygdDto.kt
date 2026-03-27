package no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev

import no.nav.pensjon.brev.alder.model.vedlegg.AfpPrivatSokerUforeTrygdVedleggDto
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText

data class AfpPrivatSokerUforeTrygdDto(
    override val saksbehandlerValg: SaksBehandlerValg,
    override val pesysData: EmptyFagsystemdata = EmptyFagsystemdata,
    val vedleggData: AfpPrivatSokerUforeTrygdVedleggDto,
) : RedigerbarBrevdata<AfpPrivatSokerUforeTrygdDto.SaksBehandlerValg, EmptyFagsystemdata> {

    data class SaksBehandlerValg(
        @DisplayText("Bruker har ikke søkt UforeTrygd")
        val harIkkeSoktUforeTrygd: Boolean,
    ) : SaksbehandlerValgBrevdata
}