package no.nav.pensjon.brev.api.model.maler.vedlegg

import no.nav.pensjon.brev.api.model.Sakstype

data class DineRettigheterOgMulighetTilAaKlageDto(
    val sakstype: Sakstype,
    val brukerUnder18Aar: Boolean?,
)