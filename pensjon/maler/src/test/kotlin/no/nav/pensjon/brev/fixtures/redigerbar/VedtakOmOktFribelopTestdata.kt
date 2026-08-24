package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmOktFribelopAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmOktFribelopData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmOktFribelopRedigerbarDto
import no.nav.pensjon.brev.fixtures.createMaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.fixtures.createOrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

fun createVedtakOmOktFribelopData() =
    VedtakOmOktFribelopData(
        bunnfradrag = Kroner(200000),
        fribelop = Kroner(136549),
        oktFribelopHeleAret = false,
        pe = createPEgruppe10(),
        maanedligUfoeretrygdFoerSkatt = createMaanedligUfoeretrygdFoerSkattDto(),
        orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto(),
        datoOkningBunnfradrag = LocalDate.of(2026, 10, 1)
    )

fun createVedtakOmOktFribelopAutoDto() =
    VedtakOmOktFribelopAutoDto(
        vedtakData = createVedtakOmOktFribelopData(),
    )

fun createVedtakOmOktFribelopRedigerbarDto() =
    VedtakOmOktFribelopRedigerbarDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = VedtakOmOktFribelopRedigerbarDto.PesysData(
            vedtakData = createVedtakOmOktFribelopData(),
        )
    )
