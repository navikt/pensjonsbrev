package no.nav.pensjon.brev.api.model.maler.ufoerApi

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata

data class VarselOpphoerMedHvilendeRettDto(
    val etteroppgjorsar: Int
) : BrevbakerBrevdata