package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.Sakstype

data class DineRettigheterOgMulighetTilAaKlageDto(
    val saktype: Sakstype,
    val bruker_brukerUnder18Ar: Boolean
) {
    constructor() : this(
        saktype = Sakstype.UFOEREP,
        bruker_brukerUnder18Ar = false
    )
}
