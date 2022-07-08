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
        barnetillegg = MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned.BeloepMedAvkortning(Kroner(75), Kroner(100)),
        dekningFasteUtgifter = Kroner(0),
        garantitilleggNordisk27 = MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned.BeloepMedAvkortning(Kroner(75), Kroner(100)),
        grunnbeloep = Kroner(0),
        ordinaerUTBeloep = MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned.BeloepMedAvkortning(Kroner(75), Kroner(100)),
        totalUTBeloep = MaanedligUfoeretrygdFoerSkattDto.UfoeretrygdPerMaaned.BeloepMedAvkortning(Kroner(75), Kroner(100)),
        virkningFraOgMed = LocalDate.of(2020, 1, 1),
        virkningTilOgMed = LocalDate.of(2020, 1, 2),
        erAvkortet = true,
    )