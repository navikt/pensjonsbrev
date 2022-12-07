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
    //TODO legg til støtte for interfaces i template-model-generator.
    data class Barnetillegg(
        override val utbetalt: Boolean,
        val antallBarn: Int, // TODO remove after version transition
        override val inntektstak: Kroner,
        override val gjelderFlereBarn: Boolean,
    ): InnvilgetBarnetillegg

}