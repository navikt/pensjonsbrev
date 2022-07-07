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
        val barnetillegg: BeloepMedAvkortning?,
        val dekningFasteUtgifter: Kroner?,
        val garantitilleggNordisk27: BeloepMedAvkortning?,
        val grunnbeloep: Kroner,
        val ordinaerUTBeloep: BeloepMedAvkortning,
        val totalUTBeloep: BeloepMedAvkortning,
        val virkningFraOgMed: LocalDate,
        val virkningTilOgMed: LocalDate?,
        val erAvkortet: Boolean,
    ) {

        data class BeloepMedAvkortning(
            val netto: Kroner,
            val brutto: Kroner,
        )
    }
}
