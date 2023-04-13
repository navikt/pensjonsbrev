package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDto
import java.time.LocalDate

@Suppress("unused")
fun createEndringIOpptjeningAutoDto() =
    EndringIOpptjeningAutoDto(
        avkortningsinformasjon = Fixtures.create(),
        beregningUfoereEndringIOpptjening = Fixtures.create(),
        fellesbarnTillegg = Fixtures.create(),
        harEktefelletilleggInnvilget = true,
        harGjenlevendetilleggInnvilget = true,
        harYrkesskadeOppfylt = false,
        maanedligUfoeretrygdFoerSkatt = Fixtures.create(),
        opplysningerBruktIBeregningUT = Fixtures.create(),
        orienteringOmRettigheterUfoere = Fixtures.create(),
        saerkullsbarnTillegg = Fixtures.create(),
        sivilstand = Sivilstand.GIFT,
        virkningsDato = LocalDate.of(2023, 1, 1),
        brukerBorInorge = true
    )

fun createEndringIOpptjeningAutoDtoAkortningsinformasjon() =
    EndringIOpptjeningAutoDto.Avkortningsinformasjon(
        beloepsgrense = Kroner(60000),
        forventetInntekt = Kroner(400000),
        harFullUtbetalingsgrad = false,
        harInntektEtterUfoere = true,
        harUtbetalingsgrad = true,
        inntektsgrense = Kroner(300000),
        inntektsgrenseNesteAar = Kroner(380000),
        inntektstak = Kroner(0),
        kompensasjonsgrad = 60.00,
        oppjustertInntektEtterUfoere = Kroner(320000),
        oppjustertInntektFoerUfoere = Kroner(0),
        oppjustertInntektFoerUfoere80prosent = Kroner(260000),
        utbetalingsgrad = 80,
    )

fun createEndringIOpptjeningAutoDtoBeregningUfoereEndringIOpptjening() =
    EndringIOpptjeningAutoDto.BeregningUfoereEndringIOpptjening(
        grunnbeloep = Kroner(111477),  // 1G 2023
        harBeloepOekt = false,
        harBeloepRedusert = true,
        harDelvisUfoeregrad = true,
        harFullUfoeregrad = false,
        harUfoeretrygdUtbetalt = true,
        nettoAkkumulerteBeloepUtbetalt = Kroner(380000),
        nettoAkkumulertePlussNettoRestAar = Kroner(80000),
        nettoTilUtbetalingRestenAvAaret = Kroner(55000),
        nettoUfoeretrygdUtbetaltPerMaaned = Kroner(12000),
        ufoeregrad = 80,
        ufoertrygdUtbetalt = Kroner(2000000),
        utbetaltPerMaaned = Kroner(16000),
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