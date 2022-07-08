package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDto
import java.time.LocalDate

fun createUngUfoerAutoDto() =
    UngUfoerAutoDto(
        kravVirkningFraOgMed = LocalDate.now(),
        totaltUfoerePerMnd = Kroner(9000),
        ektefelle = null,
        gjenlevende = UngUfoerAutoDto.InnvilgetTillegg(true),
        fellesbarn = UngUfoerAutoDto.InnvilgetBarnetillegg(false, 1, Kroner(10_000)),
        saerkullsbarn = UngUfoerAutoDto.InnvilgetBarnetillegg(true, 2, Kroner(10_000)),
        minsteytelseVedVirkSats = 2.91,
        maanedligUfoeretrygdFoerSkatt = Fixtures.create(),
        orienteringOmRettigheterUfoere = Fixtures.create(),
    )