package no.nav.pensjon.brev.alder.model.aldersovergang

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata

data class InfoAldersovergangEps60AarAutoDto(
    val ytelse: Ytelse
) : BrevbakerBrevdata

enum class Ytelse {
    ALDER,
    AFP;
}