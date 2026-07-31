package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmOktBunnfradragAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmOktBunnfradragData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmOktBunnfradragRedigerbarDto
import no.nav.pensjon.brev.fixtures.createMaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.fixtures.createOrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10

fun createVedtakOmOktBunnfradragData() =
    VedtakOmOktBunnfradragData(
        pe = createPEgruppe10(),
        maanedligUfoeretrygdFoerSkatt = createMaanedligUfoeretrygdFoerSkattDto(),
        orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto()
    )

fun createVedtakOmOktBunnfradragAutoDto() =
    VedtakOmOktBunnfradragAutoDto(
        vedtakData = createVedtakOmOktBunnfradragData(),
    )

fun createVedtakOmOktBunnfradragRedigerbarDto() =
    VedtakOmOktBunnfradragRedigerbarDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = VedtakOmOktBunnfradragRedigerbarDto.PesysData(
            vedtakData = createVedtakOmOktBunnfradragData(),
        )
    )
