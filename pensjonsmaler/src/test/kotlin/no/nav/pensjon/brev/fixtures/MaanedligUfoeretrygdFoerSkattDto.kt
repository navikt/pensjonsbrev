package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createMaanedligUfoeretrygdFoerSkattDto() =
    MaanedligUfoeretrygdFoerSkattDto(
        ufoeretrygdPerioder = listOf(createMaanedligUfoeretrygdFoerSkattDtoUfoeretrygdPerMaaned()),
    )

fun createMaanedligUfoeretrygdFoerSkattDtoUfoeretrygdPerMaaned() =
    MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned(
        annetBelop = Kroner(0),
        barnetilleggFellesbarnBrutto = Kroner(1),
        barnetilleggFellesbarnNetto = Kroner(2),
        barnetilleggSaerkullsbarnBrutto = Kroner(3),
        barnetilleggSaerkullsbarnNetto = Kroner(4),
        dekningFasteUtgifter = Kroner(5),
        ektefelletilleggBrutto = Kroner(6),
        ektefelletilleggNetto = Kroner(7),
        erAvkortet = true,
        garantitilleggNordisk27Brutto = Kroner(8),
        garantitilleggNordisk27Netto = Kroner(9),
        gjenlevendetilleggBrutto = Kroner(10),
        gjenlevendetilleggNetto = Kroner(11),
        grunnbeloep = Kroner(12),
        ordinaerUTBeloepBrutto = Kroner(13),
        ordinaerUTBeloepNetto = Kroner(14),
        totalUTBeloepBrutto = Kroner(15),
        totalUTBeloepNetto = Kroner(16),
        virkningFraOgMed = LocalDate.of(2020, 1, 1),
        virkningTilOgMed = LocalDate.of(2020, 1, 2),
    )
