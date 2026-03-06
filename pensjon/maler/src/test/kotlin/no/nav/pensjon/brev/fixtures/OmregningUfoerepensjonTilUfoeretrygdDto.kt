package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.OmregningUfoerepensjonTilUfoeretrygdDto

fun createOmregningUfoerepensjonTilUfoeretrygdDto() =
    OmregningUfoerepensjonTilUfoeretrygdDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = OmregningUfoerepensjonTilUfoeretrygdDto.PesysData(
            pe = createPE(),
            maanedligUfoeretrygdFoerSkatt = Fixtures.createVedlegg()
        )
    )
