package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.vedlegg.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class UngUfoerAutoDto(
    val kravVirkningFraOgMed: LocalDate,
    val totaltUfoerePerMnd: Kroner,
    val ektefelle: InnvilgetTillegg?,
    val gjenlevende: InnvilgetTillegg?,
    val fellesbarn: Barnetillegg?,
    val saerkullsbarn: Barnetillegg?,
    val minsteytelseVedVirkSats: Double,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
) : BrevbakerBrevdata {
    data class InnvilgetTillegg(val utbetalt: Boolean)

    data class Barnetillegg(
        val utbetalt: Boolean,
        val inntektstak: Kroner,
        val gjelderFlereBarn: Boolean,
    )
}
