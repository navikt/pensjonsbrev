package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.legacy.OkningGrad2026
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
        manedligOkningUforetrygdUtAret = Kroner(3500),
        uforegrad = 100,
        datoOkningBunnfradrag = LocalDate.of(2026, 6, 1),
        ieu = Kroner(63451),
        antallMnd1g = 6,
        redusertBtfb = true,
        bunnfradrag2027 = Kroner(200000),
        toArFor2026 = false,
        toArI2026ForForsteOktober = false,
        okningGrad2026 = OkningGrad2026(
            dato = LocalDate.of(2026, 6, 1),
            gammelUforegrad = 80,
            gammelIEU = Kroner(150000),
            antallMndGammelIEU = 7
        ),
        okningUt = true,
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
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = VedtakOmOktBunnfradragRedigerbarDto.PesysData(
            vedtakData = createVedtakOmOktBunnfradragData(),
        )
    )
