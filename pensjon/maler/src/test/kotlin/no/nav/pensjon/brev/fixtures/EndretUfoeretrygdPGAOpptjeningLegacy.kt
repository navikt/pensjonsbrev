package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.legacy.EndretUforetrygdPGAOpptjeningLegacyDto

fun createEndretUforetrygdPGAOpptjeningLegacyDto() =
    EndretUforetrygdPGAOpptjeningLegacyDto(
        pe = createPEgruppe10(),
        maanedligUfoeretrygdFoerSkatt = createMaanedligUfoeretrygdFoerSkattDto(),
        orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto(),
        harVTA = false,
    )