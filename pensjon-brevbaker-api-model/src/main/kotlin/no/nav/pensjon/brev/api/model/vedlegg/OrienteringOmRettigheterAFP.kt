package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sivilstand

data class OrienteringOmRettigheterAfpDto(
    val bruker_sivilstand: Sivilstand,
    val bruker_borINorge: Boolean,
    val institusjon_gjeldende: Institusjon,
    val saktype: Sakstype,
) {
    constructor() : this(
        bruker_sivilstand = Sivilstand.ENSLIG,
        bruker_borINorge = false,
        institusjon_gjeldende = Institusjon.INGEN,
        saktype = Sakstype.AFP,
    )
}
