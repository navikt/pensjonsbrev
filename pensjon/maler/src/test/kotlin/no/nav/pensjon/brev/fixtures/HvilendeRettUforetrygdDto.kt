package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.ufoerApi.HvilendeRettUforetrygdDto

fun createHvilendeRettUforetrygdDto() =
    HvilendeRettUforetrygdDto(
        senesteHvilendeAr = 2025,
    )
