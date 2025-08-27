package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.ufoerApi.AvslagUforetrygdDemoDto

fun createAvslagUforetrygdDemoDto() = AvslagUforetrygdDemoDto(
    saksbehandlerValg = AvslagUforetrygdDemoDto.Saksbehandlervalg(false),
    pesysData = AvslagUforetrygdDemoDto.PenData("placeholder")
)