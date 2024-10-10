package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto

data class EndretBarnetilleggUfoeretrygdDto(
    val pe: PE,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
    val barnetilleggFellesbarn: BarnetilleggFellesbarn?,
    val barnetilleggSaerkullsbarn: BarnetilleggSaerkullsbarn?,
): BrevbakerBrevdata