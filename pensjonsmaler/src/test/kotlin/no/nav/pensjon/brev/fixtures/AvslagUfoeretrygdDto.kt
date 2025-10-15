package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.AvslagUfoeretrygdDto

fun createAvslagUfoeretrygdDto() = AvslagUfoeretrygdDto(
    saksbehandlerValg = EmptySaksbehandlerValg,
    pesysData = AvslagUfoeretrygdDto.PesysData(
        pe = createPE(),
    )
)