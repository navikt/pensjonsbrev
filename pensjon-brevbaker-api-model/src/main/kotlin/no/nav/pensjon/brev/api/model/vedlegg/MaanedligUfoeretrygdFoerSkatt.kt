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
        @Deprecated("Use separate brutto/netto values") val barnetillegg: BeloepMedAvkortning?, // TODO remove in next version
        val barnetilleggBrutto: Kroner?,
        val barnetilleggNetto: Kroner?,
        val dekningFasteUtgifter: Kroner?,
        val erAvkortet: Boolean,
        @Deprecated("Use separate brutto/netto values") val garantitilleggNordisk27: BeloepMedAvkortning?, // TODO remove in next version
        val garantitilleggNordisk27Brutto: Kroner?,
        val garantitilleggNordisk27Netto: Kroner?,
        val grunnbeloep: Kroner,
        @Deprecated("Use separate brutto/netto values") val ordinaerUTBeloep: BeloepMedAvkortning, // TODO remove in next version
        val ordinaerUTBeloepBrutto: Kroner,
        val ordinaerUTBeloepNetto: Kroner,
        @Deprecated("Use separate brutto/netto values") val totalUTBeloep: BeloepMedAvkortning, // TODO remove in next version
        val totalUTBeloepBrutto: Kroner?, //TODO make mandatory in next version
        val totalUTBeloepNetto: Kroner?, //TODO make mandatory in next version
        val virkningFraOgMed: LocalDate,
        val virkningTilOgMed: LocalDate?,
    ) {

        @Deprecated("Use separate brutto/netto values")
        data class BeloepMedAvkortning(// TODO remove in next version
            val netto: Kroner,
            val brutto: Kroner,
        )
    }
}
