package no.nav.pensjon.brev.fixtures.alder

import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderAP2016AutoDto
import no.nav.pensjon.brev.alder.model.avslag.NormertPensjonsalder
import no.nav.pensjon.brev.alder.model.avslag.OpplysningerBruktIBeregningen
import no.nav.pensjon.brev.alder.model.avslag.OpplysningerBruktIBeregningenKap19
import no.nav.pensjon.brev.alder.model.avslag.OpplysningerBruktIBeregningenKap20
import no.nav.pensjon.brev.alder.model.avslag.TrygdeperiodeNorge
import no.nav.pensjon.brev.alder.model.avslag.TrygdeperiodeUtland
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Year
import java.time.LocalDate

fun createAvslagUttakFoerNormertPensjonsalderAP2016AutoDto() =
    AvslagUttakFoerNormertPensjonsalderAP2016AutoDto(
        minstePensjonssats = Kroner(215000),
        normertPensjonsalder = NormertPensjonsalder(
            aar = 67,
            maaneder = 2
        ),
        virkFom = LocalDate.of(2025, 2, 1),
        totalPensjon = Kroner(200000),
        afpBruktIBeregning = true,
        opplysningerBruktIBeregningen = createOpplysningerBruktIBeregningen(),
        borINorge = false,
        harEOSLand = false,
        avtaleland = "Danmark",
    )

fun createOpplysningerBruktIBeregningen(): OpplysningerBruktIBeregningen = OpplysningerBruktIBeregningen(
    uttaksgrad = 100,
    trygdetid = 40,
    pensjonsbeholdning = Kroner(1200000),
    delingstallVedUttak = 12.0,
    delingstallVedNormertPensjonsalder = 10.0,
    normertPensjonsalder = NormertPensjonsalder(
        aar = 67,
        maaneder = 2
    ),
    sisteOpptjeningsAar = 2024,
    virkFom = LocalDate.of(2025, 2, 1),
    prorataBruktIBeregningen = true,
    trygdeperioderNorge = listOf(
                TrygdeperiodeNorge(fom = vilkaarligDato, tom = vilkaarligDato),
                TrygdeperiodeNorge(fom = vilkaarligDato, tom = vilkaarligDato)
    ),
    trygdeperioderUtland = listOf(
        TrygdeperiodeUtland(
            land = "Sverige",
                    fom = vilkaarligDato,
                    tom = vilkaarligDato
                ), TrygdeperiodeUtland(land = "Danmark", fom = vilkaarligDato, tom = vilkaarligDato)
    ),
    kravAarsak = null,
    opplysningerKap19 = OpplysningerBruktIBeregningenKap19(
        forholdstallVed67 = 10.0,
        forholdstall = 5.0,
        poengAarE91 = 10,
        poengAarF92 = 10,
        innvilgetTillegspensjon = true,
        poengAar = 4,
        sluttpoengTall = 1.86,
        redusertTrygdetidKap19 = true,
        avslattKap19 = false,
        fodselsAar = Year(1990),
        andelGammeltRegelverk = 6,
        andelNyttRegelverk = 4,
        trygdetidKap19 = 35
    ),
    opplysningerKap20 = OpplysningerBruktIBeregningenKap20(
        redusertTrygdetidKap20 = false
    )
)
