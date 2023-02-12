package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDto
import java.time.LocalDate

@Suppress("unused")
fun createEndringIOpptjeningAutoDto() =
    EndringIOpptjeningAutoDto(
        fellesbarnTillegg = Fixtures.create(),
        saerkullsbarnTillegg = Fixtures.create(),
        endringIOpptjening = Fixtures.create(),
        sivilstand = Sivilstand.GIFT,
        maanedligUfoeretrygdFoerSkatt = Fixtures.create(),
        opplysningerBruktIBeregningUT = Fixtures.create(),
        orienteringOmRettigheterUfoere = Fixtures.create(),
    )

fun createEndringIOpptjeningAutoDtoEndringIOpptjening() =
    EndringIOpptjeningAutoDto.EndringIOpptjening(
        beloepsgrense = Kroner(60000),
        brukerBorInorge = false,
        forventetInntekt = Kroner(400000),
        grunnbeloep = Kroner(111477),  // 1G 2023
        harBeloepOekt = true,
        harBeloepRedusert = false,
        harDelvisUfoeregrad = true,
        harEktefelletilleggInnvilget = true,
        harFullUfoeregrad = false,
        harFullUtbetalingsgrad = false,
        harGjenlevendetilleggInnvilget = false,
        harInntektEtterUfoere = true,
        harUtbetalingsgrad = true,
        harYrkesskadeGradUtbetaling = false,
        inntektsgrense = Kroner(300000),
        inntektsgrenseNesteAar = Kroner(380000),
        inntektstak = Kroner(0),
        kompensasjonsgrad = 60,
        nettoAkkumulerteBeloepUtbetalt = Kroner(380000),
        nettoAkkumulertePlussNettoRestAar = Kroner(435000),
        nettoTilUtbetalingRestenAvAaret = Kroner(55000),
        nettoUfoeretrygdUtbetaltPerMaaned = Kroner(12000),
        oppjustertInntektEtterUfoere = Kroner(320000),
        oppjustertInntektFoerUfoere = Kroner(0),
        oppjustertInntektFoerUfoere80prosent = Kroner(320000),
        ufoeregrad = 80,
        ufoertrygdUtbetalt = Kroner(2000000),
        utbetalingsgrad = 70,
        utbetaltPerMaaned = Kroner(16000),
        virkningsDato = LocalDate.of(2023, 1, 1),
    )

fun createEndringIOpptjeningAutoDtoBarnetilleggFellesbarn() =
    EndringIOpptjeningAutoDto.FellesbarnTillegg(
        beloepBrutto = Kroner(50000),
        beloepNetto = Kroner(20000),
        fribeloep = Kroner(200000),
        gjelderFlereBarn = false,
        harFellesbarnInnvilget = false,
        harFradrag = false,
        harFratrukketBeloepFraAnnenForelder = false,
        harJusteringsbeloep = false,
        inntektAnnenForelder = Kroner(200000),
        inntektBruktIAvkortning = Kroner(400000),
        inntektstak = Kroner(300000),
    )

fun createEndringIOpptjeningAutoDtoBarnetilleggSaerkullsbarn() =
    EndringIOpptjeningAutoDto.SaerkullsbarnTillegg(
        beloepBrutto = Kroner(0),
        beloepNetto = Kroner(0),
        fribeloep = Kroner(0),
        gjelderFlereBarn = false,
        harFradrag = false,
        harJusteringsbeloep = false,
        harSaerkullsbarnInnvilget = false,
        inntektBruktIAvkortning = Kroner(0),
        inntektstak = Kroner(0),
    )