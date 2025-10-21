package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer

data class BekreftelsePaaMottakAvPensjonDto(
    override val pesysData: PesysData, override val saksbehandlerValg: EmptySaksbehandlerValg
) : RedigerbarBrevdata<EmptySaksbehandlerValg, BekreftelsePaaMottakAvPensjonDto.PesysData> {

    data class PesysData(
        val address: String,  //PE_PersonSak_PSstatsborger
        val foedselsnummer: Foedselsnummer,  // PE_
        val navn: String
    ) : BrevbakerBrevdata
}