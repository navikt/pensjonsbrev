package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.model.alder.adhoc.AdhocAlderspensjonGjtOppryddingAutoDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createAlderspensjonGjtOppryddingAutoDto() =
    AdhocAlderspensjonGjtOppryddingAutoDto(
        totalPensjon = Kroner(1000),
        virkFom = LocalDate.now()
    )
