package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmEndringBarnetilleggEPSData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmEndringBarnetilleggEPSRedigerbarDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

fun createVedtakOmEndringBarnetilleggEPSData() =
    VedtakOmEndringBarnetilleggEPSData(
        nettoUforetrygdUtenTillegg = Kroner(10000),
        nettoBarnetilleggFB = Kroner(2000),
        nettoBarnetilleggSB = Kroner(3000),
        totalbelop = Kroner(12000),
        samletInntektsgrenseBarnetillegg = Kroner(24000),
        fribelop = Kroner(50000),
        barnetilleggSB = true,
        opphortUforetrygdEllerBTFB = false,
        pe = Fixtures.create(),
        maanedligUfoeretrygdFoerSkatt = Fixtures.createVedlegg(),
        dineRettigheterOgPlikterUfore = Fixtures.createVedlegg(),
    )

fun createVedtakOmEndringBarnetilleggEPSAutoDto() =
    VedtakOmEndringBarnetilleggEPSAutoDto(
        vedtakData = createVedtakOmEndringBarnetilleggEPSData(),
    )

fun createVedtakOmEndringBarnetilleggEPSRedigerbarDto() =
    VedtakOmEndringBarnetilleggEPSRedigerbarDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = VedtakOmEndringBarnetilleggEPSRedigerbarDto.PesysData(
            vedtakData = createVedtakOmEndringBarnetilleggEPSData(),
        )
    )