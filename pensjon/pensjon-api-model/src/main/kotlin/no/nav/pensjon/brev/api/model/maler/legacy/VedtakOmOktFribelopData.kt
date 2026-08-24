package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class VedtakOmOktFribelopData(
    val bunnfradrag: Kroner,
    val fribelop: Kroner,
    val oktFribelopHeleAret: Boolean,
    val datoOkningBunnfradrag: LocalDate,
    val pe: PEgruppe10,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
)
