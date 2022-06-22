package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Kroner
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
    // TODO: PL-4948 - Endre til obligatorisk når pesys er oppdatert
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto?,
) {
    constructor(): this(
        kravVirkningFraOgMed = LocalDate.now(),
        totaltUfoerePerMnd = Kroner(9000),
        ektefelle = null,
        gjenlevende = InnvilgetTillegg(true),
        fellesbarn = InnvilgetBarnetillegg(false, 1, Kroner(10_000)),
        saerkullsbarn = InnvilgetBarnetillegg(true, 2, Kroner(10_000)),
        minsteytelseVedVirkSats = 2.91,
        maanedligUfoeretrygdFoerSkatt = null,
        orienteringOmRettigheterUfoere = null,
    )
    data class InnvilgetTillegg(val utbetalt: Boolean) {
        constructor(): this(false)
    }
    data class InnvilgetBarnetillegg(val utbetalt: Boolean, val antallBarn: Int, val inntektstak: Kroner) {
        constructor(): this(false, 1, Kroner(10))
    }
}