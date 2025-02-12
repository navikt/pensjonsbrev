package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto

fun createOrienteringOmRettigheterUfoereDto() =
    OrienteringOmRettigheterUfoereDto(
        bruker_borINorge = true,
        harInnvilgetBarnetilleggFellesBarn = false,
        harInnvilgetBarnetilleggSaerkullsbarn = false,
        harTilleggForFlereBarn = false,
        institusjon_gjeldende = Institusjon.INGEN,
        sivilstand = Sivilstand.ENSLIG,
    )
