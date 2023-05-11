package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDto
import java.time.LocalDate

@Suppress("unused")
fun createEndringIOpptjeningAutoDto() =
    EndringIOpptjeningAutoDto(
        brukerBorInorge = true,
        fellesbarn1 = Fixtures.create(),
        grunnbeloep = Kroner(111477),
        harEktefelletilleggInnvilget = true,
        harGjenlevendetilleggInnvilget = true,
        harOektUtbetaling = false,
        harRedusertUtbetaling = false,
        harYrkesskadeOppfylt = false,
        maanedligUfoeretrygdFoerSkatt = Fixtures.create(),
        opplysningerBruktIBeregningUT = Fixtures.create(),
        orienteringOmRettigheterUfoere = Fixtures.create(),
        saerkullsbarn1 = Fixtures.create(),
        sivilstand = Sivilstand.GIFT,
        ufoeregrad = 100,
        ufoeretrygdOrdinaer1 = Fixtures.create(),
        ufoertrygdUtbetalt = Kroner(0),
        utbetaltPerMaaned = Kroner(0),
        virkningsDato = LocalDate.of(2023, 1, 1),
        )

fun createEndringIOpptjeningAutoDtoUfoeretrygdOrdinaer1() =
    EndringIOpptjeningAutoDto.UfoeretrygdOrdinaer1(
        nettoAkkumulerteBeloepPlussNettoTilUtbetalingRestenAvAaret = Kroner(0),
        nettoAkkumulerteBeloepUtbetalt = Kroner(0),
        nettoTilUtbetalingRestenAvAaret = Kroner(0),
        nettoUfoeretrygdUtbetaltPerMaaned = Kroner(0),
        avkortningsinformasjon = EndringIOpptjeningAutoDto.UfoeretrygdOrdinaer1.Avkortningsinformasjon(
            beloepsgrense = Kroner(60000),
            forventetInntekt = Kroner(400000),
            harInntektEtterUfoere = true,
            inntektsgrense = Kroner(300000),
            inntektsgrenseNesteAar = Kroner(380000),
            inntektstak = Kroner(0),
            kompensasjonsgrad = 60.00,
            inntektsgrenseEtterUfoeretrygd = Kroner(320000),
            inntektsgrenseFoerUfoeretrygd = Kroner(0),
            inntektsgrenseFoerUfoeretrygd80prosent = Kroner(260000),
            utbetalingsgrad = 80,
        )
    )

fun createEndringIOpptjeningAutoDtoBarnetilleggFellesbarn1() =
    EndringIOpptjeningAutoDto.Fellesbarn1(
        beloepBrutto = Kroner(50000),
        beloepNetto = Kroner(0),
        brukersInntektBruktIAvkortning = Kroner(300000),
        fribeloep = Kroner(200000),
        gjelderFlereBarn = true,
        harFradrag = true,
        harFratrukketBeloepFraAnnenForelder = true,
        harJusteringsbeloep = false,
        inntektAnnenForelder = Kroner(200000),
        inntektstak = Kroner(300000),
        samletInntektBruktIAvkortning = Kroner(550000),
    )

fun createEndringIOpptjeningAutoDtoBarnetilleggSaerkullsbarn1() =
    EndringIOpptjeningAutoDto.Saerkullsbarn1(
        beloepBrutto = Kroner(80000),
        beloepNetto = Kroner(0),
        brukersInntektBruktIAvkortning = Kroner(500000),
        fribeloep = Kroner(200000),
        gjelderFlereBarn = false,
        harFradrag = false,
        harJusteringsbeloep = false,
        inntektstak = Kroner(300000),
    )