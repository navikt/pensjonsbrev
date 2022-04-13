package no.nav.pensjon.brev.api.model.vedlegg

import java.time.LocalDate

data class MaanedligeUfoeretrygdFoerSkattDto(
    val gjeldendeBeregnetUTPerMaaned: UfoeretrygdPerMaaned,
    val virkningDatoFraOgMed_krav: LocalDate,
    val antallBeregningsperioderPaaVedtak: Int,
    val ufoeretrygdPerioder: List<UfoeretrygdPerMaaned>,
) {
    constructor() : this(
        gjeldendeBeregnetUTPerMaaned = UfoeretrygdPerMaaned(
            annetBelop = 0,
            barnetillegg = 0,
            dekningFasteUtgifter = 0,
            garantitilleggNordisk27 = 0,
            grunnbeloep = 0,
            ordinaerUTBeloep = 0,
            totalUTBeloep = 0,
            virkningFraOgMed = LocalDate.of(2020, 1, 1),
            virkningTilOgMed = LocalDate.of(2020, 1, 2),
            avkortning = null,
        ),
        virkningDatoFraOgMed_krav = LocalDate.of(2020,1,1),
        antallBeregningsperioderPaaVedtak = 4,
        ufoeretrygdPerioder = emptyList(),
    )
    data class UfoeretrygdPerMaaned(
        val annetBelop: Int,
        val barnetillegg: Int?,
        val dekningFasteUtgifter: Int,
        val garantitilleggNordisk27: Int?,
        val grunnbeloep: Int,
        val ordinaerUTBeloep: Int,
        val totalUTBeloep: Int,
        val virkningFraOgMed: LocalDate,
        val virkningTilOgMed: LocalDate?,
        val avkortning: Avkortning?,
    ) {
        data class Avkortning(
            val barnetilleggFoerAvkort: Int,
            val garantitilleggNordisk27FoerAvkort: Int,
            val ordinaerUTBeloepFoerAvkort: Int,
            val totalUTBeloepFoerAvkort: Int,
        )
    }
}