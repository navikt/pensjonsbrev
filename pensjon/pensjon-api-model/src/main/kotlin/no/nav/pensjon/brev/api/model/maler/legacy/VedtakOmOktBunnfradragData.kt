package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class VedtakOmOktBunnfradragData(
    val uforetrygd: Kroner,
    val barnetillegg: Kroner?,
    val bunnfradrag: Kroner,
    val fribelop: Kroner,
    val ekstraManedligUfoeretrygdUtAret: Kroner?,
    val uforegrad: Int,
    val oktFribelopHeleAret: Boolean,
    val datoOkning: LocalDate,
    val ieu: Kroner,
    val antallMnd1g: Int,
    val oktUforetrygd: Boolean,
    val redusertBarnetillegg: Boolean,
    val pe: PEgruppe10,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
)
