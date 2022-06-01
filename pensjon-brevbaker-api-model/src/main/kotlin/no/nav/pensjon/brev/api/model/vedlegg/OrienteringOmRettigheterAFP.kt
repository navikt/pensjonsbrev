package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.Sivilstand

data class OrienteringOmRettigheterAfpDto(
    val bruker_sivilstand: Sivilstand,
    val bruker_borINorge: Boolean,
    val institusjon_gjeldende: Institusjon,
) {
    constructor() : this(
        bruker_sivilstand = Sivilstand.ENSLIG,
        bruker_borINorge = false,
        institusjon_gjeldende = Institusjon.INGEN,
    )
}
