package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.legacy.FribelopPeriode
import no.nav.pensjon.brev.api.model.maler.legacy.Scenario2_1G_04G
import no.nav.pensjon.brev.api.model.maler.legacy.Scenario4_04G_1G_04G
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
        redusertBtfb = true,
        redusertBtsb = true,
        bunnfradrag2027 = Kroner(240000),
        nettoHarBlittLikBrutto = true,

        vektetFribelop = 0.7,
        vektetFribelopKr = Kroner(95584),
        scenario1_1G = false,
        scenario2_1G_04G = null,/*Scenario2_1G_04G(
            dato04G = LocalDate.of(2026, 6, 1),
            uforegradForOkning = 80,
        ),*/
        scenario3_04G_1G = false,
        scenario4_04G_1G_04G = Scenario4_04G_1G_04G(
            dato04G = LocalDate.of(2026, 6, 1),
            uforegradForOkning = 80,
        ),
        okningUt = true,
        fribelopPerioder = listOf(
            FribelopPeriode(
                fom = LocalDate.of(2026, 1, 1),
                tom = LocalDate.of(2026, 5, 31),
                uforegrad = 80,
                faktor = 1.0,
            ),
            FribelopPeriode(
                fom = LocalDate.of(2026, 6, 1),
                tom = LocalDate.of(2026, 12, 31),
                uforegrad = 100,
                faktor = 0.4,
            )
        ),
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
