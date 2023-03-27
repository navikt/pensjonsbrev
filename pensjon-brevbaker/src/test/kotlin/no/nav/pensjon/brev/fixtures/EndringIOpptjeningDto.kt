package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDto
import java.time.LocalDate

@Suppress("unused")
fun createEndringIOpptjeningAutoDto() =
    EndringIOpptjeningAutoDto(
        endringIOpptjening = Fixtures.create(),
        endringIOpptjeningBeregning = Fixtures.create(),
        endringIOpptjeningBoolean = Fixtures.create(),
        fellesbarnTillegg = Fixtures.create(),
        harEktefelletilleggInnvilget = true,
        harGjenlevendetilleggInnvilget = true,
        harYrkesskadeGradUtbetaling = false,
        maanedligUfoeretrygdFoerSkatt = Fixtures.create(),
        opplysningerBruktIBeregningUT = Fixtures.create(),
        orienteringOmRettigheterUfoere = Fixtures.create(),
        saerkullsbarnTillegg = Fixtures.create(),
        sivilstand = Sivilstand.GIFT,
        virkningsDato = LocalDate.of(2023, 1, 1),
    )

fun createEndringIOpptjeningAutoDtoEndringIOpptjening() =
    EndringIOpptjeningAutoDto.EndringIOpptjening(
        beloepsgrense = Kroner(60000),
        forventetInntekt = Kroner(400000),
        grunnbeloep = Kroner(111477),  // 1G 2023
        inntektsgrense = Kroner(300000),
        inntektsgrenseNesteAar = Kroner(380000),
        inntektstak = Kroner(0),
        kompensasjonsgrad = 60.00,
        nettoAkkumulerteBeloepUtbetalt = Kroner(380000),
        nettoTilUtbetalingRestenAvAaret = Kroner(55000),
        nettoUfoeretrygdUtbetaltPerMaaned = Kroner(12000),
        oppjustertInntektEtterUfoere = Kroner(320000),
        oppjustertInntektFoerUfoere = Kroner(0),
        ufoeregrad = 80,
        ufoertrygdUtbetalt = Kroner(2000000),
        utbetalingsgrad = 80,
        utbetaltPerMaaned = Kroner(16000)
    )

fun createEndringIOpptjeningBeregning() =
    EndringIOpptjeningAutoDto.EndringIOpptjeningBeregning(
        nettoAkkumulertePlussNettoRestAar = Kroner(435000),
        oppjustertInntektFoerUfoere80prosent = Kroner(320000),
    )


fun createEndringIOpptjeningAutoDtoEndringIOpptjeningBoolean() =
    EndringIOpptjeningAutoDto.EndringIOpptjeningBoolean(
        brukerBorInorge = false,
        harBeloepOekt = false,
        harBeloepRedusert = true,
        harDelvisUfoeregrad = true,
        harFullUfoeregrad = false,
        harFullUtbetalingsgrad = false,
        harInntektEtterUfoere = true,
        harUtbetalingsgrad = true,
        harUfoeretrygdUtbetalt = true,
    )

fun createEndringIOpptjeningAutoDtoBarnetilleggFellesbarn() =
    EndringIOpptjeningAutoDto.FellesbarnTillegg(
        beloepBrutto = Kroner(50000),
        beloepNetto = Kroner(0),
        fribeloep = Kroner(200000),
        gjelderFlereBarn = true,
        harFellesbarnInnvilget = true,
        harFradrag = true,
        harFratrukketBeloepFraAnnenForelder = true,
        harJusteringsbeloep = false,
        inntektAnnenForelder = Kroner(200000),
        inntektBruktIAvkortning = Kroner(30),
        inntektstak = Kroner(300000),
    )

fun createEndringIOpptjeningAutoDtoBarnetilleggSaerkullsbarn() =
    EndringIOpptjeningAutoDto.SaerkullsbarnTillegg(
        beloepBrutto = Kroner(80000),
        beloepNetto = Kroner(0),
        fribeloep = Kroner(200000),
        gjelderFlereBarn = false,
        harFradrag = false,
        harJusteringsbeloep = false,
        harSaerkullsbarnInnvilget = true,
        inntektBruktIAvkortning = Kroner(500000),
        inntektstak = Kroner(300000),
    )