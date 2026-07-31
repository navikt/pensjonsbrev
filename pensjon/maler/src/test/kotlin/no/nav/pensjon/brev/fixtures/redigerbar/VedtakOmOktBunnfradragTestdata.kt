package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmOktBunnfradragAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmOktBunnfradragData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmOktBunnfradragRedigerbarDto
import no.nav.pensjon.brev.fixtures.createMaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.fixtures.createOrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

fun createVedtakOmOktBunnfradragData() =
    VedtakOmOktBunnfradragData(
        uforetrygd = Kroner(26000),
        barnetillegg = Kroner(4000),
        bunnfradrag = Kroner(200000),
        fribelop = Kroner(136549),
        ekstraManedligUfoeretrygdUtAret = Kroner(3500),
        uforegrad = 100,
        oktFribelopHeleAret = false,
        datoOkning = LocalDate.of(2026, 6, 1),
        ieu = Kroner(63451),
        antallMnd1g = 7,
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
