package no.nav.pensjon.brev.alder.model.aldersovergang

import no.nav.pensjon.brev.api.model.maler.AutobrevData

data class InfoAldersovergangEps60AarAutoDto(
    val ytelse: Ytelse
) : AutobrevData

enum class Ytelse {
    ALDER,
    AFP;
}