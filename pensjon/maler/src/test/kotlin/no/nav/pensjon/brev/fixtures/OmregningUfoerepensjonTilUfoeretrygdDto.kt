package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.OmregningUfoerepensjonTilUfoeretrygdDto

fun createOmregningUfoerepensjonTilUfoeretrygdDto() =
    OmregningUfoerepensjonTilUfoeretrygdDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = OmregningUfoerepensjonTilUfoeretrygdDto.PesysData(
            pe = createPEgruppe10(),
            maanedligUfoeretrygdFoerSkatt = Fixtures.createVedlegg()
        )
    )
