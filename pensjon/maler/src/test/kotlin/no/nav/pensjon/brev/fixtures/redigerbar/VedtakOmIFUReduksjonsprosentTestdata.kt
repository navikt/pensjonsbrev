package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.legacy.UTTillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentData
import no.nav.pensjon.brev.fixtures.createMaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.fixtures.createOrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

fun createVedtakOmIFUReduksjonsprosentAutoDto() =
    VedtakOmIFUReduksjonsprosentData(
        nettoUforetrygdUtenTillegg = Kroner(26000),
        nettoBarnetillegg = Kroner(4000),
        totalbelop = Kroner(30000),
        etterbetalingJuli = Kroner(1500),
        reduksjonsprosent = 50.0,
        inntektstak = Kroner(500000),
        ifu = Kroner(300000),
        tillegg = listOf(UTTillegg.BT, UTTillegg.GJT),
        endringNettoUforetrygdUtenTillegg = true,
        endringNettoBarnetillegg = true,
        endringInntektstak = true,
        erInntektsavkortet = true,
        hjemler = setOf("12-13", "12-16", "12-18", "22-12"),
        pe = createPEgruppe10(),
        maanedligUfoeretrygdFoerSkatt = createMaanedligUfoeretrygdFoerSkattDto(),
        orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto(),
    )