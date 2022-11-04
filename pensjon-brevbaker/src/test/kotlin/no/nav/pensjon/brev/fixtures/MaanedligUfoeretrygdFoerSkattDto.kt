package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import java.time.LocalDate

fun createMaanedligUfoeretrygdFoerSkattDto() =
    MaanedligUfoeretrygdFoerSkattDto(
        gjeldendeBeregnetUTPerMaaned = Fixtures.create(),
        krav_virkningsDatoFraOgMed = LocalDate.of(2020, 1, 1),
        tidligereUfoeretrygdPerioder = emptyList(),
    )

fun createMaanedligUfoeretrygdFoerSkattDtoUfoeretrygdPerMaaned() =
    MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned(
        annetBelop = Kroner(0),
        barnetilleggFellesbarnBrutto = Kroner(0),
        barnetilleggFellesbarnNetto = Kroner(0),
        barnetilleggSaerkullsbarnBrutto = Kroner(0),
        barnetilleggSaerkullsbarnNetto = Kroner(0),
        dekningFasteUtgifter = Kroner(0),
        ektefelletilleggBrutto = Kroner(0),
        ektefelletilleggNetto = Kroner(0),
        erAvkortet = true,
        garantitilleggNordisk27Brutto = Kroner(0),
        garantitilleggNordisk27Netto = Kroner(0),
        gjenlevendetilleggBrutto = Kroner(0),
        gjenlevendetilleggNetto = Kroner(0),
        grunnbeloep = Kroner(0),
        ordinaerUTBeloepBrutto = Kroner(0),
        ordinaerUTBeloepNetto = Kroner(0),
        totalUTBeloepBrutto = Kroner(0),
        totalUTBeloepNetto = Kroner(0),
        virkningFraOgMed = LocalDate.of(2020, 1, 1),
        virkningTilOgMed = LocalDate.of(2020, 1, 2),
    )