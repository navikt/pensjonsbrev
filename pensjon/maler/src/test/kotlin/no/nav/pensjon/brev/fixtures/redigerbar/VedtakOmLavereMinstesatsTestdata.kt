package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.legacy.Tillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsData
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsAutoDto
import no.nav.pensjon.brev.fixtures.createMaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.fixtures.createOrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

fun createVedtakOmLavereMinstesatsData() =
    VedtakOmLavereMinstesatsData(
        nettoUforetrygdUtenTillegg = Kroner(26000),
        nettoBarnetillegg = Kroner(4000),
        nettoGjenlevendetillegg = Kroner(2000),
        egenopptjentUforetrygd = Kroner(26001),
        reduksjonsprosent = 50.0,
        harMinstesats = true,
        tidligereMinstesats = Kroner(300000),
        nyMinstesats = Kroner(290000),
        avkortetPgaRedusertTrygdetid = false,
        harGradertUfoeretrygd = false,
        tillegg = listOf(Tillegg.BT, Tillegg.GJT),
        endringNettoUforetrygdUtenTillegg = true,
        endringNettoBarnetillegg = true,
        endringNettoGjenlevendetillegg = true,
        endringReduksjonsprosent = true,
        hjemmeltekst = "§§ 12-13 til 12-16, 12-18 og 22-12",
        pe = createPEgruppe10(),
        maanedligUfoeretrygdFoerSkatt = createMaanedligUfoeretrygdFoerSkattDto(),
        orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto()
    )

fun createVedtakOmLavereMinstesatsAutoDto() =
    VedtakOmLavereMinstesatsAutoDto(
        vedtakData = createVedtakOmLavereMinstesatsData(),
    )