package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.EndretBarnetilleggUfoeretrygdDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createEndretBarnetilleggUfoeretrygdDto() =
    EndretBarnetilleggUfoeretrygdDto(
        pe = Fixtures.create(),
        maanedligUfoeretrygdFoerSkatt = Fixtures.create(),
        orienteringOmRettigheterUfoere = Fixtures.create(),

    )