package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class VedtakOmOktBunnfradragData(
    val uforetrygd: Kroner,                      
    val barnetillegg: Kroner?,                   
    val fribelop: Kroner,
    val bunnfradrag: Kroner,
    val bunnfradrag2027: Kroner,
    val uforegrad: Int,
    val manedligOkningUforetrygdUtAret: Kroner?,
    val toArI2026ForForsteOktober: Boolean,
    val toArFor2026: Boolean,
    val okningGrad2026: OkningGrad2026?,
    val antallMnd1g: Int,
    val okningUt: Boolean,
    val oktUforetrygd: Boolean,
    val redusertBarnetillegg: Boolean,
    val datoOkningBunnfradrag: LocalDate,
    val ieu: Kroner,

    val pe: PEgruppe10,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
)

data class OkningGrad2026(
    val dato: LocalDate,
    val gammelUforegrad: Int,
    val gammelIEU: Kroner
)