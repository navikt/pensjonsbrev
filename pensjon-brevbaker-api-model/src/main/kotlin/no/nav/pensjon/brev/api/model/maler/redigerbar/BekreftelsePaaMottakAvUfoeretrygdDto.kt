package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer

data class BekreftelsePaaMottakAvUfoeretrygdDto(
    override val pesysData: PesysData, override val saksbehandlerValg: EmptySaksbehandlerValg
) : RedigerbarBrevdata<EmptySaksbehandlerValg, BekreftelsePaaMottakAvUfoeretrygdDto.PesysData> {

    data class PesysData(
        val navn: String,
        val foedselsnummer: Foedselsnummer,
        val statsborgerskap: String
    ) : BrevbakerBrevdata
}