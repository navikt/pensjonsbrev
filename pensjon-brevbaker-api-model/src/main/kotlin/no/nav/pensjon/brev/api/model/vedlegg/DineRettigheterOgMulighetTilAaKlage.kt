package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.Sakstype

@Suppress("unused")
data class DineRettigheterOgMulighetTilAaKlageDto(
    val saktype: Sakstype,
    val brukerUnder18Ar: Boolean
)
