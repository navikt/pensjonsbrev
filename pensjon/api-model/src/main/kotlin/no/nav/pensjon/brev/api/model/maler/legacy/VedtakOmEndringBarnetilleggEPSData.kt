package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgPlikterUforeDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

data class VedtakOmEndringBarnetilleggEPSData(
    val nettoUforetrygdUtenTillegg: Kroner,
    val nettoBarnetilleggFB: Kroner?,
    val nettoBarnetilleggSB: Kroner?,
    val barnetilleggSB: Boolean,
    val totalbelop: Kroner,

    val samletInntektsgrenseBarnetillegg: Kroner,
    val fribelop: Kroner,

    val pe: PEgruppe10,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val dineRettigheterOgPlikterUfore: DineRettigheterOgPlikterUforeDto,
)
