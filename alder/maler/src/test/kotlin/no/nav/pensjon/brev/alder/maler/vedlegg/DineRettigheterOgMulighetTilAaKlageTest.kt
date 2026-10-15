package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto

fun createDineRettigheterOgMulighetTilAaKlageDto() = DineRettigheterOgMulighetTilAaKlageDto(
    sakstype = Sakstype.ALDER,
    brukerUnder18Aar = null
)