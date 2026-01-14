package no.nav.pensjon.brev.fixtures

import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDto
import no.nav.pensjon.brevbaker.api.model.Kroner

fun createUngUfoerAutoDto() =
    UngUfoerAutoDto(
        kravVirkningFraOgMed = vilkaarligDato,
        totaltUfoerePerMnd = Kroner(9000),
        ektefelle = null,
        gjenlevende = UngUfoerAutoDto.InnvilgetTillegg(true),
        fellesbarn = UngUfoerAutoDto.Barnetillegg(false, Kroner(10_000), gjelderFlereBarn = false),
        saerkullsbarn = UngUfoerAutoDto.Barnetillegg(true, Kroner(10_000), gjelderFlereBarn = true),
        minsteytelseVedVirkSats = 2.91,
        maanedligUfoeretrygdFoerSkatt = Fixtures.createVedlegg(),
        orienteringOmRettigheterUfoere = Fixtures.createVedlegg(),
    )