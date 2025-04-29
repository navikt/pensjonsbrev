package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.YtelseForAldersovergangKode
import no.nav.pensjon.brev.api.model.maler.alderApi.AlderspensjonGjtOppryddingAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.InfoAlderspensjonOvergang67AarAutoDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createAlderspensjonGjtOppryddingAutoDto() =
    AlderspensjonGjtOppryddingAutoDto(
        totalPensjon = Kroner(1000),
        virkFom = LocalDate.now()
    )
