package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.phrases.UfoeretrygdBarnetilleggIkkeUtbetalt
import no.nav.pensjon.brev.api.model.vedlegg.*
import java.time.LocalDate

@Suppress("unused")
data class UngUfoerAutoDto(
    val kravVirkningFraOgMed: LocalDate,
    val totaltUfoerePerMnd: Kroner,
    val ektefelle: InnvilgetTillegg?,
    val gjenlevende: InnvilgetTillegg?,
    val fellesbarn: InnvilgetBarnetillegg?,
    val saerkullsbarn: InnvilgetBarnetillegg?,
    val minsteytelseVedVirkSats: Double,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
) {
    data class InnvilgetTillegg(val utbetalt: Boolean)
    data class InnvilgetBarnetillegg(
        override val utbetalt: Boolean,
        override val antallBarn: Int,
        override val inntektstak: Kroner
    ):UfoeretrygdBarnetilleggIkkeUtbetalt

}