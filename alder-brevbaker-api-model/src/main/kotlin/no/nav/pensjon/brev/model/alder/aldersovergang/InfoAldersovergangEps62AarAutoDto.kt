package no.nav.pensjon.brev.model.alder.aldersovergang

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata

data class InfoAldersovergangEps62AarAutoDto(
    val ytelse: YtelseType
) : BrevbakerBrevdata

enum class YtelseType {
    ALDER,
    AFP;
}