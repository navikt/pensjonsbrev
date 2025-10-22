package no.nav.pensjon.brev.model.alder.vedlegg

import no.nav.pensjon.brev.api.model.Sakstype

data class DineRettigheterOgMulighetTilAaKlageDto(
    val sakstype: Sakstype,
    val brukerUnder18Aar: Boolean?,
)