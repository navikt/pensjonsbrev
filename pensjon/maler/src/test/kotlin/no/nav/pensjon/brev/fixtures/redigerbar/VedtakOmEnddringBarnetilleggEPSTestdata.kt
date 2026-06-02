package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

fun createVedtakOmEndringBarnetilleggEPSAutoDto() =
    VedtakOmEndringBarnetilleggEPSAutoDto(
        vedtakData = VedtakOmEndringBarnetilleggEPSData(
            nettoUforetrygdUtenTillegg = Kroner(10000),
            nettoBarnetilleggFB = Kroner(2000),
            totalbelop = Kroner(12000),
            inntektBruker = Kroner(200000),
            inntektEPS = Kroner(150000),
            gInntil = Kroner(600000),
            samletInntekt = Kroner(350000),
            samletInntektsgrenseBarnetillegg = Kroner(24000),
            nyttBarnetillegg = Kroner(2000),
            fribelop = Kroner(50000),
            arligUtbetalingBarnetilleggFB = Kroner(24000),
            utbetaltBarnetilleggHittilIAr = Kroner(12000),
            utbetalingBarnetilleggResten = Kroner(6000),
            pe = Fixtures.create(),
            maanedligUfoeretrygdFoerSkatt = Fixtures.createVedlegg(),
            dineRettigheterOgPlikterUfore = Fixtures.createVedlegg(),
        )
    )