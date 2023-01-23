package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto

fun createOrienteringOmRettigheterUfoereDto() =
    OrienteringOmRettigheterUfoereDto(
        bruker_borINorge = false,
        harTilleggForFlereBarn = true,
        institusjon_gjeldende = Institusjon.INGEN,
        avdoed_sivilstand = Sivilstand.ENSLIG,
        harInnvilgetBarnetillegg = true,
    )