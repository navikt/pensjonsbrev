package no.nav.pensjon.brev.api.model.maler

import java.time.LocalDate

data class UngUfoerAutoDto(
    val kravVirkningFraOgMed: LocalDate,
    val totaltUfoerePerMnd: Int,
    val ektefelle: InnvilgetTillegg?,
    val gjenlevende: InnvilgetTillegg?,
    val fellesbarn: InnvilgetBarnetillegg?,
    val saerkullsbarn: InnvilgetBarnetillegg?,
    val minsteytelseVedVirkSats: Double
) {
    data class InnvilgetTillegg(val utbetalt: Boolean)
    data class InnvilgetBarnetillegg(val utbetalt: Boolean, val antallBarn: Int, val inntektstak: Int)
}