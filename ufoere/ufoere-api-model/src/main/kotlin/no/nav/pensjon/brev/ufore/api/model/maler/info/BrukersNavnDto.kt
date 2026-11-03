package no.nav.pensjon.brev.ufore.api.model.maler.info

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

data class BrukersNavnDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: BrukersNavnPendata,
) : RedigerbarBrevdata<BrukersNavnDto.BrukersNavnPendata> {

    data class BrukersNavnPendata(
        val navn: String?,
    ) : FagsystemBrevdata
}
