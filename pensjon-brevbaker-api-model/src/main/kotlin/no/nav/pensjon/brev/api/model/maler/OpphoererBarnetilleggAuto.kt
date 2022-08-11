package no.nav.pensjon.brev.api.model.maler

@Suppress("unused")
data class OpphoererBarnetilleggAutoDto(
    val barnetilleggFellesbarnInnvilget: Boolean,
    val barnetilleggSaerkullsbarnInnvilget: Boolean,
)