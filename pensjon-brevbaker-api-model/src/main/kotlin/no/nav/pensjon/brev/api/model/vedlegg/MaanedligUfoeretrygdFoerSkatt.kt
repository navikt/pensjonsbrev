package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned.*
import java.time.LocalDate

data class MaanedligUfoeretrygdFoerSkattDto(
    val gjeldendeBeregnetUTPerMaaned: UfoeretrygdPerMaaned,
    val krav_virkningsDatoFraOgMed: LocalDate,
    val tidligereUfoeretrygdPerioder: List<UfoeretrygdPerMaaned>,
) {
    constructor() : this(
        gjeldendeBeregnetUTPerMaaned = UfoeretrygdPerMaaned(),
        krav_virkningsDatoFraOgMed = LocalDate.of(2020, 1, 1),
        tidligereUfoeretrygdPerioder = emptyList(),
    )

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
        constructor() : this(
            annetBelop = Kroner(0),
            barnetillegg = BeloepMedAvkortning(Kroner(75), Kroner(100)),
            dekningFasteUtgifter = Kroner(0),
            garantitilleggNordisk27 = BeloepMedAvkortning(Kroner(75), Kroner(100)),
            grunnbeloep = Kroner(0),
            ordinaerUTBeloep = BeloepMedAvkortning(Kroner(75), Kroner(100)),
            totalUTBeloep = BeloepMedAvkortning(Kroner(75), Kroner(100)),
            virkningFraOgMed = LocalDate.of(2020, 1, 1),
            virkningTilOgMed = LocalDate.of(2020, 1, 2),
            erAvkortet = true,
        )

        data class BeloepMedAvkortning(
            val netto: Kroner,
            val brutto: Kroner,
        ) {
            constructor(): this(Kroner(1), Kroner(10))
        }
    }
}
