package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.Sivilstand

data class OrienteringOmRettigheterUfoereDto(
    val bruker_borINorge: Boolean,
    val institusjon_gjeldende: Institusjon,
    val avdoed_sivilstand: Sivilstand,
    val ufoeretrygdPerMaaned_barnetilleggGjeldende: Int?,
) {
    constructor() : this(
        bruker_borINorge = false,
        institusjon_gjeldende = Institusjon.INGEN,
        avdoed_sivilstand = Sivilstand.ENSLIG,
        ufoeretrygdPerMaaned_barnetilleggGjeldende = 0,
    )
}

