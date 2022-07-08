package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto

fun createOrienteringOmRettigheterUfoereDto() =
    OrienteringOmRettigheterUfoereDto(
        bruker_borINorge = false,
        institusjon_gjeldende = Institusjon.INGEN,
        avdoed_sivilstand = Sivilstand.ENSLIG,
        ufoeretrygdPerMaaned_barnetilleggGjeldende = Kroner(0),
    )