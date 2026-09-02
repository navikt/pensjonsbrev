package no.nav.pensjon.brev.alder.maler.avslag

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.alder.maler.sivilstand.createMaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.alder.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.alder.maler.vedlegg.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.alder.model.BeloepEndring
import no.nav.pensjon.brev.alder.model.KravInitiertAv
import no.nav.pensjon.brev.alder.model.avslag.AvslagPaaGjenlevenderettIAlderspensjonDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Percent

fun createAvslagPaaGjenlevenderettIAlderspensjon() = AvslagPaaGjenlevenderettIAlderspensjonDto(
    saksbehandlerValg = lagSaksbehandlervalg(
        "samboerUtenFellesBarn" to false,
        "avdoedNavn" to "Peder Ås",
        "underEttAarsMedlemstidEOESEllerAvtaleland" to true,
        "underTreFemAarsMedlemstidNasjonalSak" to false,
        "underTreFemAarsMedlemstidEOESSak" to false,
        "underTrefemAarsMedlemstidAvtalesak" to true,
        "under20AarBotid" to true,
        "ekteskapUnderFemAar" to true,
        "hjemmelEOES" to true,
        "hjemmelAvtaleland" to false,
        "harTrygdetid" to true,
    ),
    pesysData = AvslagPaaGjenlevenderettIAlderspensjonDto.PesysData(
        alderspensjonVedVirk = AvslagPaaGjenlevenderettIAlderspensjonDto.PesysData.AlderspensjonVedVirk(
            totalPensjon = Kroner(1_000_000), uttaksgrad = Percent(50)
        ),
        krav = AvslagPaaGjenlevenderettIAlderspensjonDto.PesysData.Krav(
            kravInitiertAv = KravInitiertAv.BRUKER
        ),
        avdoed = AvslagPaaGjenlevenderettIAlderspensjonDto.PesysData.Avdoed(
            harTrygdetidNorge = false,
            harTrygdetidEOS = false,
            harTrygdetidAvtaleland = true
        ),
        ytelseskomponentInformasjon = AvslagPaaGjenlevenderettIAlderspensjonDto.PesysData.YtelseskomponentInformasjon(
            beloepEndring = BeloepEndring.ENDR_OKT
        ),
        beregnetPensjonPerMaaned = AvslagPaaGjenlevenderettIAlderspensjonDto.PesysData.BeregnetPensjonPerManed(
            antallBeregningsperioderPensjon = 4
        ),
        avtaleland = AvslagPaaGjenlevenderettIAlderspensjonDto.PesysData.Avtaleland(
            erEOSLand = false,
            navn = "Togo"
        ),
        bruker = AvslagPaaGjenlevenderettIAlderspensjonDto.PesysData.Bruker(
            faktiskBostedsland = "Benin"
        ),
        dineRettigheterOgMulighetTilAaKlage = createDineRettigheterOgMulighetTilAaKlageDto(),
        maanedligPensjonFoerSkatt = createMaanedligPensjonFoerSkatt(),
        maanedligPensjonFoerSkattAP2025 = createMaanedligPensjonFoerSkattAP2025Dto()
    )
)