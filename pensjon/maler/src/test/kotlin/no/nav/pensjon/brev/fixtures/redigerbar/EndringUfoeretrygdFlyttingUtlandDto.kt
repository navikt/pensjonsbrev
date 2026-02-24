package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdFlyttingUtlandDto
import no.nav.pensjon.brev.fixtures.createPE

fun createEndringUfoeretrygdFlyttingUtlandDto() =
    EndringUfoeretrygdFlyttingUtlandDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = EndringUfoeretrygdFlyttingUtlandDto.PesysData(
            pe = createPE(),
            opphortEktefelletillegg = true,
            opphortBarnetillegg = true,

            maanedligUfoeretrygdFoerSkatt = null
        ),
    )
