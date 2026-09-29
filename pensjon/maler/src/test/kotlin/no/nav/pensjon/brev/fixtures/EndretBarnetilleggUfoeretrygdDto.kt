package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.legacy.EndretBarnetilleggUfoeretrygdDto

fun createEndretBarnetilleggUfoeretrygdDto() =
    EndretBarnetilleggUfoeretrygdDto(
        pe = createPEgruppe10(),
        maanedligUfoeretrygdFoerSkatt = createMaanedligUfoeretrygdFoerSkattDto(),
        orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto(),
    )