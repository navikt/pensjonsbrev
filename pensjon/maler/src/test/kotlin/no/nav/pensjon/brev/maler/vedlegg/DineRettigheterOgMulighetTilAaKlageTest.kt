package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto

fun createDineRettigheterOgMulighetTilAaKlageDto() = DineRettigheterOgMulighetTilAaKlageDto(
    sakstype = Sakstype.ALDER,
    brukerUnder18Aar = null
)