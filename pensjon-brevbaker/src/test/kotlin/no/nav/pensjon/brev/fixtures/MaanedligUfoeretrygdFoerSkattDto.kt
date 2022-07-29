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
        dekningFasteUtgifter = Kroner(0),
        grunnbeloep = Kroner(0),
        virkningFraOgMed = LocalDate.of(2020, 1, 1),
        virkningTilOgMed = LocalDate.of(2020, 1, 2),
        erAvkortet = true,
        barnetilleggBrutto = Kroner(0),
        barnetilleggNetto = Kroner(0),
        garantitilleggNordisk27Brutto = Kroner(0),
        garantitilleggNordisk27Netto = Kroner(0),
        ordinaerUTBeloepBrutto = Kroner(0),
        ordinaerUTBeloepNetto = Kroner(0),
        totalUTBeloepBrutto = Kroner(0),
        totalUTBeloepNetto = Kroner(0),
    )