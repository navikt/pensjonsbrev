package no.nav.pensjon.brev.alder.maler.avslag

import no.nav.pensjon.brev.alder.maler.sivilstand.createMaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.alder.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.alder.maler.vedlegg.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.alder.model.BeloepEndring
import no.nav.pensjon.brev.alder.model.KravInitiertAv
import no.nav.pensjon.brev.alder.model.avslag.AvslagPaaGjenlevenderettIAlderspensjonDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Percent

fun createAvslagPaaGjenlevenderettIAlderspensjon() = AvslagPaaGjenlevenderettIAlderspensjonDto(
    alderspensjonVedVirk = AvslagPaaGjenlevenderettIAlderspensjonDto.AlderspensjonVedVirk(
        totalPensjon = Kroner(1_000_000), uttaksgrad = Percent(50)
    ),
    krav = AvslagPaaGjenlevenderettIAlderspensjonDto.Krav(
        kravInitiertAv = KravInitiertAv.BRUKER
    ),
    avdoed = AvslagPaaGjenlevenderettIAlderspensjonDto.Avdoed(
        harTrygdetidNorge = false,
        harTrygdetidEOS = false,
        harTrygdetidAvtaleland = true
    ),
    ytelseskomponentInformasjon = AvslagPaaGjenlevenderettIAlderspensjonDto.YtelseskomponentInformasjon(
        beloepEndring = BeloepEndring.ENDR_OKT
    ),
    beregnetPensjonPerMaaned = AvslagPaaGjenlevenderettIAlderspensjonDto.BeregnetPensjonPerManed(
        antallBeregningsperioderPensjon = 4
    ),
    avtaleland = AvslagPaaGjenlevenderettIAlderspensjonDto.Avtaleland(
        erEOSLand = false,
        navn = "Togo"
    ),
    bruker = AvslagPaaGjenlevenderettIAlderspensjonDto.Bruker(
        faktiskBostedsland = "Benin"
    ),
    dineRettigheterOgMulighetTilAaKlage = createDineRettigheterOgMulighetTilAaKlageDto(),
    maanedligPensjonFoerSkatt = createMaanedligPensjonFoerSkatt(),
    maanedligPensjonFoerSkattAP2025 = createMaanedligPensjonFoerSkattAP2025Dto()
)