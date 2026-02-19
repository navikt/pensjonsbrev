package no.nav.pensjon.brev.fixtures.alder

import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brev.alder.model.adhoc.AdhocAlderspensjonGjtOppryddingAutoDto
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner

fun createAlderspensjonGjtOppryddingAutoDto() =
    AdhocAlderspensjonGjtOppryddingAutoDto(
        totalPensjon = Kroner(1000),
        virkFom = vilkaarligDato
    )
