package no.nav.pensjon.brev.alder.model.aldersovergang

import no.nav.pensjon.brevbaker.api.model.maler.AutobrevData

data class InfoAldersovergangEps62AarAutoDto(
    val ytelse: YtelseType
) : AutobrevData

enum class YtelseType {
    ALDER,
    AFP;
}