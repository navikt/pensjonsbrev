package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brevbaker.api.model.maler.VedleggData

data class OrienteringOmRettigheterUfoereDto(
    val bruker_borINorge: Boolean,
    val harTilleggForFlereBarn: Boolean,
    val harInnvilgetBarnetilleggFellesBarn: Boolean,
    val harInnvilgetBarnetilleggSaerkullsbarn: Boolean,
    val institusjon_gjeldende: Institusjon,
) : VedleggData