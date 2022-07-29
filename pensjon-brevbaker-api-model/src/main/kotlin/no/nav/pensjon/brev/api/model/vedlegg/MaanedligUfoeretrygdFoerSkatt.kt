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
        val barnetillegg: BeloepMedAvkortning?, // TODO remove in next version
        val barnetilleggBrutto: Kroner?,
        val barnetilleggNetto: Kroner?,
        val dekningFasteUtgifter: Kroner?,
        val erAvkortet: Boolean,
        val garantitilleggNordisk27: BeloepMedAvkortning?, // TODO remove in next version
        val garantitilleggNordisk27Brutto: Kroner?,
        val garantitilleggNordisk27Netto: Kroner?,
        val grunnbeloep: Kroner,
        val ordinaerUTBeloep: BeloepMedAvkortning, // TODO remove in next version
        val ordinaerUTBeloepBrutto: Kroner,
        val ordinaerUTBeloepNetto: Kroner,
        val totalUTBeloep: BeloepMedAvkortning, // TODO remove in next version
        val totalUTBeloepBrutto: Kroner,
        val totalUTBeloepNetto: Kroner,
        val virkningFraOgMed: LocalDate,
        val virkningTilOgMed: LocalDate?,
    ) {

        // TODO remove in next version
        data class BeloepMedAvkortning(
            val netto: Kroner,
            val brutto: Kroner,
        )
    }
}
