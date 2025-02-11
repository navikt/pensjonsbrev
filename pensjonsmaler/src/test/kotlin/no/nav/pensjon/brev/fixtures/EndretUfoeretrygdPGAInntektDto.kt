package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.EndretUfoeretrygdPGAInntektDto

fun createEndretUfoeretrygdPGAInntektDto() =
    EndretUfoeretrygdPGAInntektDto(
        pe = Fixtures.create(),
        maanedligUfoeretrygdFoerSkatt = Fixtures.create(),
        orienteringOmRettigheterUfoere = Fixtures.create(),
    )
