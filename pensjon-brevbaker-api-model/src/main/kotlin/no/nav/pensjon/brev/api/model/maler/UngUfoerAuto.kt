package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.phrases.InnvilgetBarnetillegg
import no.nav.pensjon.brev.api.model.vedlegg.*
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
) {
    data class InnvilgetTillegg(val utbetalt: Boolean)
    data class Barnetillegg(
        override val utbetalt: Boolean,
        val antallBarn: Int,
        override val inntektstak: Kroner,
        override val gjelderFlereBarn: Boolean,
    ): InnvilgetBarnetillegg

}