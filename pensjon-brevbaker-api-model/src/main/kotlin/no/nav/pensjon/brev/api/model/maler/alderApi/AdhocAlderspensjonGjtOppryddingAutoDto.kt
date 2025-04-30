package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class AdhocAlderspensjonGjtOppryddingAutoDto(
    val totalPensjon: Kroner,
    val virkFom: LocalDate
) : BrevbakerBrevdata