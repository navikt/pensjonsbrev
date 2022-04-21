package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned.*
import java.time.LocalDate

data class MaanedligUfoeretrygdFoerSkattDto(
    val gjeldendeBeregnetUTPerMaaned: UfoeretrygdPerMaaned,
    val virkningDatoFraOgMed_krav: LocalDate,
    val antallBeregningsperioderPaaVedtak: Int,
    val ufoeretrygdPerioder: List<UfoeretrygdPerMaaned>,
) {
    constructor() : this(
        gjeldendeBeregnetUTPerMaaned = UfoeretrygdPerMaaned(
            annetBelop = 0,
            barnetillegg = Beloep(75, 100),
            dekningFasteUtgifter = 0,
            garantitilleggNordisk27 = Beloep(75, 100),
            grunnbeloep = 0,
            ordinaerUTBeloep = Beloep(75, 100),
            totalUTBeloep = Beloep(75, 100),
            virkningFraOgMed = LocalDate.of(2020, 1, 1),
            virkningTilOgMed = LocalDate.of(2020, 1, 2),
            erAvkortet = true,
        ),
        virkningDatoFraOgMed_krav = LocalDate.of(2020, 1, 1),
        antallBeregningsperioderPaaVedtak = 4,
        ufoeretrygdPerioder = emptyList(),
    )

    data class UfoeretrygdPerMaaned(
        val annetBelop: Int,
        val barnetillegg: Beloep?,
        val dekningFasteUtgifter: Int,
        val garantitilleggNordisk27: Beloep?,
        val grunnbeloep: Int,
        val ordinaerUTBeloep: Beloep,
        val totalUTBeloep: Beloep,
        val virkningFraOgMed: LocalDate,
        val virkningTilOgMed: LocalDate?,
        val erAvkortet: Boolean,
    ) {
        data class Beloep(
            val netto: Int,
            val brutto: Int,
        )
    }
}
