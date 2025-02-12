package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class MaanedligUfoeretrygdFoerSkattDto(
    val ufoeretrygdPerioder: List<UfoeretrygdPerMaaned>,
) {
    data class UfoeretrygdPerMaaned(
        val annetBelop: Kroner,
        val barnetilleggSaerkullsbarnBrutto: Kroner?,
        val barnetilleggSaerkullsbarnNetto: Kroner?,
        val barnetilleggFellesbarnBrutto: Kroner?,
        val barnetilleggFellesbarnNetto: Kroner?,
        val gjenlevendetilleggBrutto: Kroner?,
        val gjenlevendetilleggNetto: Kroner?,
        val ektefelletilleggBrutto: Kroner?,
        val ektefelletilleggNetto: Kroner?,
        val dekningFasteUtgifter: Kroner?,
        val erAvkortet: Boolean,
        val garantitilleggNordisk27Brutto: Kroner?,
        val garantitilleggNordisk27Netto: Kroner?,
        val grunnbeloep: Kroner,
        val ordinaerUTBeloepBrutto: Kroner,
        val ordinaerUTBeloepNetto: Kroner,
        val totalUTBeloepBrutto: Kroner,
        val totalUTBeloepNetto: Kroner,
        val virkningFraOgMed: LocalDate,
        val virkningTilOgMed: LocalDate?,
    )
}
