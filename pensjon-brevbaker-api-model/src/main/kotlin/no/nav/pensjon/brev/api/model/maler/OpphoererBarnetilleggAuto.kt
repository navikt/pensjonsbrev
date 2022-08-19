package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import java.time.LocalDate

@Suppress("unused")


data class OpphoererBarnetilleggAutoDto(
    val kravVirkningFraOgMed: LocalDate,
    val totaltUfoerePerMnd: Kroner,
    val ektefelle: InnvilgetTillegg?,
    val gjenlevende: InnvilgetTillegg?,
    val fellesbarn: InnvilgetBarnetillegg?,
    val saerkullsbarn: InnvilgetBarnetillegg?,
    val minsteytelseVedVirkSats: Double,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
) {
    data class InnvilgetTillegg(val utbetalt: Boolean)
    data class InnvilgetBarnetillegg(val utbetalt: Boolean, val antallBarn: Int, val inntektstak: Kroner)
    data class OensketVirkningsDate(val oensketVirkningsDato: LocalDate)
    data class BTfribeloep(val saerkullsbarnFribeloep: Kroner, val fellesbarnFribeloep: Kroner)

}