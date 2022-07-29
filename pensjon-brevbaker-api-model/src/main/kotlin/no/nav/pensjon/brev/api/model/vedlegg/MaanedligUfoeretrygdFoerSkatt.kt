package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.Kroner
import java.time.LocalDate

data class MaanedligUfoeretrygdFoerSkattDto(
    val gjeldendeBeregnetUTPerMaaned: UfoeretrygdPerMaaned,
    val krav_virkningsDatoFraOgMed: LocalDate,
    val tidligereUfoeretrygdPerioder: List<UfoeretrygdPerMaaned>,
) {
    data class UfoeretrygdPerMaaned(
        val annetBelop: Kroner,
        val barnetilleggBrutto: Kroner?,
        val barnetilleggNetto: Kroner?,
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
