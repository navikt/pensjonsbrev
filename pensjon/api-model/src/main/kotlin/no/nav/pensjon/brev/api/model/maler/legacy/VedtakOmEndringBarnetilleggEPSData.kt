package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

data class VedtakOmEndringBarnetilleggEPSData(
    val nettoUforetrygdUtenTillegg: Kroner,
    val nettoBarnetilleggFB: Kroner?,
    val totalbelop: Kroner,

    val inntektBruker: Kroner,
    val inntektEPS: Kroner,

    val gInntil: Kroner,
    val samletInntekt: Kroner,
    val samletInntektsgrenseBarnetillegg: Kroner,
    val nyttBarnetillegg: Kroner,
    val fribelop: Kroner,
    val arligUtbetalingBarnetilleggFB: Kroner,
    val utbetaltBarnetilleggHittilIAr: Kroner,
    val utbetalingBarnetilleggResten: Kroner,

    val pe: PEgruppe10,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
)
