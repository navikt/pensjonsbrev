package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import java.time.LocalDate

data class BekreftelsePaaUfoeretrygdDto(
    val foedselsdato: LocalDate, // PE_PersonSak_PSfødselsdato
    val navn: String // PE_PersonSak_PSNavn
) : FagsystemBrevdata