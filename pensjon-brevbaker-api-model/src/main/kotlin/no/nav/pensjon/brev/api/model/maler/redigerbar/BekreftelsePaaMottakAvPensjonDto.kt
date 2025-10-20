package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer

data class BekreftelsePaaMottakAvPensjonDto (
    val navn: String,
    val foedselsnummer: Foedselsnummer,
    val statsborgerskap: String
): BrevbakerBrevdata