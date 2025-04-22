package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.maler.alderApi.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year
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
        opplysningerBruktIBeregningen = OpplysningerBruktIBeregningen(
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
                TrygdeperiodeNorge(fom = LocalDate.now(), tom = LocalDate.now()),
                TrygdeperiodeNorge(fom = LocalDate.now(), tom = LocalDate.now())
            ),
            trygdeperioderUtland = listOf(
                TrygdeperiodeUtland(
                    land = "Sverige",
                    fom = LocalDate.now(),
                    tom = LocalDate.now()
                ), TrygdeperiodeUtland(land = "Danmark", fom = LocalDate.now(), tom = LocalDate.now())
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
        ),
        borINorge = false,
        harEOSLand = true,
        vedtakBegrunnelseLavOpptjening = true,
    )
