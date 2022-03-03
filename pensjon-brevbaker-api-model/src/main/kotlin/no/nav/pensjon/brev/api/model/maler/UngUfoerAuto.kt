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
    constructor(): this(
        kravVirkningFraOgMed = LocalDate.now(),
        totaltUfoerePerMnd = 9000,
        ektefelle = null,
        gjenlevende = InnvilgetTillegg(true),
        fellesbarn = InnvilgetBarnetillegg(false, 1, 10_000),
        saerkullsbarn = InnvilgetBarnetillegg(true, 2, 10_000),
        minsteytelseVedVirkSats = 2.91
    )
    data class InnvilgetTillegg(val utbetalt: Boolean)
    data class InnvilgetBarnetillegg(val utbetalt: Boolean, val antallBarn: Int, val inntektstak: Int)
}