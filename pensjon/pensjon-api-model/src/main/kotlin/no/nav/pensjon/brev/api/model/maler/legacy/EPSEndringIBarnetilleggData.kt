package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgPlikterUforeDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class EPSEndringIBarnetilleggData(
    // TODO Sjekk om vi trenger optional felter her
    val virkningstidspunkt: LocalDate,
    val nettoUforetrygdUtenTillegg: Kroner,
    val nettoBarnetilleggFB: Kroner,
    val nettoBarnetilleggSB: Kroner,
    val totalbelop: Kroner,
    val samletInntektsgrenseBarnetillegg: Kroner,
    val fribelop: Kroner,

    val barnetilleggSB: Boolean,
    val opphortUforetrygdEllerBTFB: Boolean,

    val pe: PEgruppe10,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val dineRettigheterOgPlikterUfore: DineRettigheterOgPlikterUforeDto,
)
