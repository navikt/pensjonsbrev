package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDto
import no.nav.pensjon.brev.model.alder.avslag.NormertPensjonsalder
import no.nav.pensjon.brev.model.alder.avslag.OpplysningerBruktIBeregningen
import no.nav.pensjon.brev.model.alder.avslag.OpplysningerBruktIBeregningenKap20
import no.nav.pensjon.brev.model.alder.avslag.TrygdeperiodeNorge
import no.nav.pensjon.brev.model.alder.avslag.TrygdeperiodeUtland
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createAvslagUttakFoerNormertPensjonsalderAutoDto() =
    AvslagUttakFoerNormertPensjonsalderAutoDto(
        minstePensjonssats = Kroner(215000),
        normertPensjonsalder = NormertPensjonsalder(
            aar = 67,
            maaneder = 2
        ),
        virkFom = LocalDate.of(2025, 2, 1),
        totalPensjon = Kroner(200000),
        afpBruktIBeregning = true,
        opplysningerBruktIBeregningen = OpplysningerBruktIBeregningen(
            uttaksgrad = 80,
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
            opplysningerKap19 = null,
            opplysningerKap20 = OpplysningerBruktIBeregningenKap20(
                redusertTrygdetidKap20 = true
            ),
        ),
        borINorge = false,
        harEOSLand = false,
        avtaleland = "Danmark",
    )
