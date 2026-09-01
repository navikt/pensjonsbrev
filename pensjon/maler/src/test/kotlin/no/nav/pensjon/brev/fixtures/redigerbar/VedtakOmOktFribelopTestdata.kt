package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.legacy.FribelopPeriode
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
        oktFribelopHeleAret = false,
        pe = createPEgruppe10(),
        datoOkningBunnfradrag = LocalDate.of(2026, 10, 1),
        vektetFribelop = 1.0,
        fribelopPerioder = listOf(
            FribelopPeriode(
                fom = LocalDate.of(2026, 1, 1),
                tom = LocalDate.of(2026, 12, 31),
                uforegrad = 100,
                faktor = 1.0
            )
        ),
        normertPensjonsdatoFor2028 = false,
        maanedligUfoeretrygdFoerSkatt = createMaanedligUfoeretrygdFoerSkattDto(),
        orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto(),
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
