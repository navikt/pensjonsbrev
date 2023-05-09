package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.Sivilstand

data class OrienteringOmRettigheterUfoereDto(
    val avdoed_sivilstand: Sivilstand, // TODO fjern i neste versjon
    val sivilstand: Sivilstand,
    val bruker_borINorge: Boolean,
    val harTilleggForFlereBarn: Boolean,
    val harInnvilgetBarnetillegg: Boolean,
    val institusjon_gjeldende: Institusjon,
)