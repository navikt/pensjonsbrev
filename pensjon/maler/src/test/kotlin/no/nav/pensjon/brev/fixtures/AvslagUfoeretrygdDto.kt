package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.AvslagUfoeretrygdDto

fun createAvslagUfoeretrygdDto() = AvslagUfoeretrygdDto(
    pe = createPEgruppe10(),
)