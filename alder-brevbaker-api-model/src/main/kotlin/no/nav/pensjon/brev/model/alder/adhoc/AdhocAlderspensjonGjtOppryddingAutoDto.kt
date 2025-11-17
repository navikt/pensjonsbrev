package no.nav.pensjon.brev.model.alder.adhoc

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class AdhocAlderspensjonGjtOppryddingAutoDto(
    val totalPensjon: Kroner,
    val virkFom: LocalDate
) : AutobrevData