package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.EndretBarnetilleggUfoeretrygdDto

fun createEndretBarnetilleggUfoeretrygdDto() =
    EndretBarnetilleggUfoeretrygdDto(
        pe = Fixtures.createVedlegg(),
        maanedligUfoeretrygdFoerSkatt = Fixtures.createVedlegg(),
        orienteringOmRettigheterUfoere = Fixtures.createVedlegg(),
    )