package no.nav.pensjon.brev.ufore.api.model.maler.legacy

data class OrienteringOmRettigheterUfoereDto(
    val bruker_borINorge: Boolean,
    val harTilleggForFlereBarn: Boolean,
    val harInnvilgetBarnetilleggFellesBarn: Boolean,
    val harInnvilgetBarnetilleggSaerkullsbarn: Boolean,
    val institusjon_gjeldende: Institusjon,
)