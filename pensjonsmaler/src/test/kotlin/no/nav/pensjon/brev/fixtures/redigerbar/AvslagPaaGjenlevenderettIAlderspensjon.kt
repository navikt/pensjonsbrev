package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.KravInitiertAv
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDto
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkattAP2025
import no.nav.pensjon.brev.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Percent

fun createAvslagPaaGjenlevenderettIAlderspensjon() = AvslagPaaGjenlevenderettIAlderspensjonDto(
    saksbehandlerValg = AvslagPaaGjenlevenderettIAlderspensjonDto.SaksbehandlerValg(
        samboerUtenFellesBarn = false,
        avdoedNavn = "Peder Ås"
    ),
    pesysData = AvslagPaaGjenlevenderettIAlderspensjonDto.PesysData(
        alderspensjonVedVirk = AvslagPaaGjenlevenderettIAlderspensjonDto.PesysData.AlderspensjonVedVirk(
            totalPensjon = Kroner(1_000_000), uttaksgrad = Percent(50)
        ),
        krav = AvslagPaaGjenlevenderettIAlderspensjonDto.PesysData.Krav(
            kravInitiertAv = KravInitiertAv.BRUKER
        ),
        avdoed = AvslagPaaGjenlevenderettIAlderspensjonDto.PesysData.Avdoed(
            redusertTrygdetidNorge = true,
            redusertTrygdetidEOS = true,
            redusertTrygdetidAvtaleland = true
        ),
        ytelseskomponentInformasjon = AvslagPaaGjenlevenderettIAlderspensjonDto.PesysData.YtelseskomponentInformasjon(
            beloepEndring = BeloepEndring.ENDR_OKT
        ),
        beregnetPensjonPerManed = AvslagPaaGjenlevenderettIAlderspensjonDto.PesysData.BeregnetPensjonPerManed(
            antallBeregningsperioderPensjon = 4
        ),
        avtaleland = AvslagPaaGjenlevenderettIAlderspensjonDto.PesysData.Avtaleland(
            erEOSLand = false,
            navn = "Togo"
        ),
        bruker = AvslagPaaGjenlevenderettIAlderspensjonDto.PesysData.Bruker(
            faktiskBostedsland = "Benin"
        ),
        dineRettigheterOgMulighetTilAaKlageDto = createDineRettigheterOgMulighetTilAaKlageDto(),
        maanedligPensjonFoerSkattDto = createMaanedligPensjonFoerSkatt(),
        maanedligPensjonFoerSkattAP2025Dto = createMaanedligPensjonFoerSkattAP2025()
    )
)