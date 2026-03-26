package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdFlyttingUtlandDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10

fun createEndringUfoeretrygdFlyttingUtlandDto() =
    EndringUfoeretrygdFlyttingUtlandDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = EndringUfoeretrygdFlyttingUtlandDto.PesysData(
            pe = createPEgruppe10(),
            opphortEktefelletillegg = true,
            opphortBarnetillegg = true,

            maanedligUfoeretrygdFoerSkatt = null
        ),
    )
