package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createUngUfoerAutoDto() =
    UngUfoerAutoDto(
        kravVirkningFraOgMed = LocalDate.now(),
        totaltUfoerePerMnd = Kroner(9000),
        ektefelle = null,
        gjenlevende = UngUfoerAutoDto.InnvilgetTillegg(true),
        fellesbarn = UngUfoerAutoDto.Barnetillegg(false, Kroner(10_000), gjelderFlereBarn = false),
        saerkullsbarn = UngUfoerAutoDto.Barnetillegg(true, Kroner(10_000), gjelderFlereBarn = true),
        minsteytelseVedVirkSats = 2.91,
        maanedligUfoeretrygdFoerSkatt = Fixtures.create(),
        orienteringOmRettigheterUfoere = Fixtures.create(),
    )