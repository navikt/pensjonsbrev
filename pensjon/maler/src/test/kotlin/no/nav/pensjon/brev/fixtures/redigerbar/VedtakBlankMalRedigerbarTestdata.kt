package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakBlankMalRedigerbarDto
import no.nav.pensjon.brev.fixtures.createMaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.fixtures.createOrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10

fun createVedtakBlankMalRedigerbarDto() =
    VedtakBlankMalRedigerbarDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = VedtakBlankMalRedigerbarDto.PesysData(
            pe = createPEgruppe10(),
            maanedligUfoeretrygdFoerSkatt = createMaanedligUfoeretrygdFoerSkattDto(),
            orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto(),
        ),
    )
