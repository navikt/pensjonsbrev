package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto

@Suppress("unused")
data class EtteroppgjoerEtterbetalingAutoDto(
    val pe: PE,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
): BrevbakerBrevdata
