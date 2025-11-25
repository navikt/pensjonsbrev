package no.nav.pensjon.brev.fixtures.alder

import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderAutoDto
import no.nav.pensjon.brev.alder.model.avslag.NormertPensjonsalder
import no.nav.pensjon.brev.alder.model.avslag.OpplysningerBruktIBeregningen
import no.nav.pensjon.brev.alder.model.avslag.OpplysningerBruktIBeregningenKap20
import no.nav.pensjon.brev.alder.model.avslag.TrygdeperiodeNorge
import no.nav.pensjon.brev.alder.model.avslag.TrygdeperiodeUtland
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
            opplysningerKap19 = null,
            opplysningerKap20 = OpplysningerBruktIBeregningenKap20(
                redusertTrygdetidKap20 = true
            ),
        ),
        borINorge = false,
        harEOSLand = false,
        avtaleland = "Danmark",
    )
