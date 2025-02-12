package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.AvslagUfoeretrygdDto

fun createAvslagUfoeretrygdDto() = AvslagUfoeretrygdDto(
    saksbehandlerValg = EmptyBrevdata,
    pesysData = AvslagUfoeretrygdDto.PesysData(
        pe = createPE(),
    )
)