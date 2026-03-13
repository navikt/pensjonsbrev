package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDto
import no.nav.pensjon.brev.fixtures.createOrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

fun createInnvilgelseUfoeretrygdDto() =
    InnvilgelseUfoeretrygdDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = InnvilgelseUfoeretrygdDto.PesysData(
            pe = createPEgruppe10(),
            oifuVedVirkningstidspunkt = Kroner(10000),
            maanedligUfoeretrygdFoerSkatt = null,
            createOrienteringOmRettigheterUfoereDto()
        ),
    )
