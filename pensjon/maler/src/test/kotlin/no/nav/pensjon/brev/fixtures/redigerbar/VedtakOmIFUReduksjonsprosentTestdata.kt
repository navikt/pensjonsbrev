package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.legacy.UTTillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentData
import no.nav.pensjon.brev.fixtures.createMaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.fixtures.createOrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

private fun createVedtakOmIFUReduksjonsprosentData(visOktMinsteIFU: Boolean, visReduksjonsprosent: Boolean) =
    VedtakOmIFUReduksjonsprosentData(
        nettoUforetrygdUtenTillegg = Kroner(26000),
        nettoBarnetillegg = Kroner(4000),
        nettoGjenlevendetillegg = Kroner(2000),
        etterbetalingJuli = Kroner(1500),
        reduksjonsprosent = 50.0,
        inntektstak = Kroner(500000),
        ifu = Kroner(300000),
        tillegg = listOf(UTTillegg.BT, UTTillegg.GJT),
        endringNettoUforetrygdUtenTillegg = true,
        endringNettoBarnetillegg = true,
        endringNettoGjenlevendetillegg = true,
        endringReduksjonsprosent = visReduksjonsprosent,
        endringInntektstak = visReduksjonsprosent,
        endringIfu = visOktMinsteIFU,
        hjemler = setOf("12-13", "12-16", "12-18", "22-12"),
        visOktMinusteIFU = visOktMinsteIFU,
        visReduksjonsprosent = visReduksjonsprosent,
        pe = createPEgruppe10(),
        maanedligUfoeretrygdFoerSkatt = createMaanedligUfoeretrygdFoerSkattDto(),
        orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto(),
    )

fun createVedtakOmIFUReduksjonsprosentAutoDto() =
    VedtakOmIFUReduksjonsprosentAutoDto(
        vedtakData = createVedtakOmIFUReduksjonsprosentData(visOktMinsteIFU = true, visReduksjonsprosent = true),
    )