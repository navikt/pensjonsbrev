package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Kroner

@Suppress("unused")

data class BarnetilleggUtbetaltDto(
//  Vedtaksdata.BeregningsData.BeregningYtelsesKomp.BarnetilleggFelles.BTFBnetto
    val barnetilleggFellesbarnUtbetaltNetto: Kroner,
//  Vedtaksdata.BeregningsData.BeregningYtelsesKomp.BarnetilleggFelles.BTSBnetto
    val barnetilleggSaerkullsbarnUtbetaltNetto: Kroner
)

data class OpphoererBarnetilleggAutoDto(
    val barnetilleggFellesbarnInnvilget: Boolean,
    val barnetilleggSaerkullsbarnInnvilget: Boolean,
)