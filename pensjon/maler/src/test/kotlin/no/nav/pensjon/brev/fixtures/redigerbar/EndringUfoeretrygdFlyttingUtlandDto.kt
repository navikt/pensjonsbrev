package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdFlyttingUtlandDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10

fun createEndringUfoeretrygdFlyttingUtlandDto() =
    EndringUfoeretrygdFlyttingUtlandDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = EndringUfoeretrygdFlyttingUtlandDto.PesysData(
            pe = createPEgruppe10(),
            opphortEktefelletillegg = true,
            opphortBarnetillegg = true,

            maanedligUfoeretrygdFoerSkatt = null
        ),
    )
