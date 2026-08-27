package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class VedtakOmOktBunnfradragData(
    val uforetrygd: Kroner,
    val barnetillegg: Kroner?,
    val gjenlevendetillegg: Kroner?,
    val fribelop: Kroner,
    val bunnfradrag: Kroner,
    val bunnfradrag2027: Kroner,
    val uforegrad: Int,
    val manedligOkningUforetrygdInklTilleggUtAret: Kroner,
    val okningUt: Boolean,
    val redusertBtfb: Boolean,
    val redusertBtsb: Boolean,
    val datoOkningBunnfradrag: LocalDate,
    val nettoUtHarBlittLikBrutto: Boolean,
    val btHarBlitt0: Boolean,
    val vektetFribelop: Double,
    val vektetFribelopKr: Kroner,
    val fribelopPerioder: List<FribelopPeriode>,

    val scenario1_1G: Boolean,
    val scenario2_1G_04G: Scenario2_1G_04G?,
    val scenario4_04G_1G_04G: Scenario4_04G_1G_04G?,
    val scenario3_04G_1G: Boolean,
    val pe: PEgruppe10,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
)


data class Scenario2_1G_04G(
    val dato04G: LocalDate,
    val uforegradForOkning: Int,
)

data class Scenario4_04G_1G_04G(
    val dato04G: LocalDate,
    val uforegradForOkning: Int,
)

data class FribelopPeriode(
    val fom: LocalDate,
    val tom: LocalDate,
    val uforegrad: Int,
    val faktor: Double
)