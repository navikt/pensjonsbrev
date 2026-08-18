package no.nav.pensjon.brev.fixtures

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.AvslagUfoeretrygdDto

fun createAvslagUfoeretrygdDto() = AvslagUfoeretrygdDto(
    saksbehandlerValg = lagSaksbehandlervalg(),
    pesysData = AvslagUfoeretrygdDto.PesysData(
        pe = createPEgruppe10(),
    )
)