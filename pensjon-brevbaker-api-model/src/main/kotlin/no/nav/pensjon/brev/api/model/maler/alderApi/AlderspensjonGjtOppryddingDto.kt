package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner

@Suppress("unused")
data class AlderspensjonGjtOppryddingDto(
    val totalPensjon: Kroner
) : BrevbakerBrevdata