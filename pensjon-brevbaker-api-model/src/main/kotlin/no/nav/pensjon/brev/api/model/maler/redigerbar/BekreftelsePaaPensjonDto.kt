package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import java.time.LocalDate

data class BekreftelsePaaPensjonDto(
    override val pesysData: PesysData, override val saksbehandlerValg: EmptySaksbehandlerValg
) : RedigerbarBrevdata<EmptySaksbehandlerValg, BekreftelsePaaPensjonDto.PesysData> {

    data class PesysData(
        val foedselsdato: LocalDate,  // PE_PersonSak_PSfødselsdato
        val navn: String  // PE_PersonSak_PSNavn
    ) : FagsystemBrevdata
}