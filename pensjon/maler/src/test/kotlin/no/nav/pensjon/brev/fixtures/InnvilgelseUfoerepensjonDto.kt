package no.nav.pensjon.brev.fixtures


import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoerepensjonDto

fun createInnvilgelseUfoerepensjonDto() =
    InnvilgelseUfoerepensjonDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = InnvilgelseUfoerepensjonDto.PesysData(
            pe = createPE(),
            maanedligUfoeretrygdFoerSkatt = Fixtures.createVedlegg()
        )
    )