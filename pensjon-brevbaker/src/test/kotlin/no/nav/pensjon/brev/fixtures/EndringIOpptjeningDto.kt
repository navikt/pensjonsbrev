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
        sivilstand = Sivilstand.SAMBOER1_5,
        ufoeretrygdEndringIOpptjening = Fixtures.create(),
        maanedligUfoeretrygdFoerSkatt = Fixtures.create(),
        opplysningerBruktIBeregningUT = Fixtures.create(),
        orienteringOmRettigheterUfoere = Fixtures.create(),
    )

fun createEndringIOpptjeningAutoDtoEndringIOpptjening() =
    EndringIOpptjeningAutoDto.EndringIOpptjening(
        beloepsgrense = Kroner(0),
        forventetInntekt = Kroner(400000),
        grunnbeloep = Kroner(111477),  // 1G 2023
        harBeloepOekt =true,
        harBeloepRedusert = false,
        harDelvisUfoeregrad = true,
        harFullUfoeregrad = false,
        harInntektEtterUfoere = false,
        inntektsgrense = Kroner(300000),
        inntektsgrenseNesteAar = Kroner(0),
        inntektstak = Kroner(0),
        kompensasjonsgrad = 0,
        nettoAkkumulerteBeloepUtbetalt = Kroner(0),
        nettoAkkumulertePlussNettoRestAar = Kroner(0),
        nettoTilUtbetalingRestenAvAaret = Kroner(0),
        nettoUfoeretrygdUtbetaltPerMaaned = Kroner(0),
        oppjustertInntektEtterUfoere = Kroner(0),
        oppjustertInntektFoerUfoere = Kroner(0),
        oppjustertInntektFoerUfoere80prosent = Kroner(0),
        ufoeregrad = 60,
        utbetalingsgrad = 50,
        virkningsDato = LocalDate.of(2023, 1, 1),
    )

fun createEndringIOpptjeningAutoDtoUfoeretrygd() =
    EndringIOpptjeningAutoDto.UfoeretrygdEndringIOpptjening(
        brukerBorInorge = false,
        harEktefelletilleggInnvilget = true,
        harFellesbarnInnvilget = false,
        harGjenlevendetilleggInnvilget = true,
        harSaerkullsbarnInnvilget = false,
        harUtbetalingsgrad = true,
        harYrkesskadeGradUtbetaling = false,
        ufoertrygdUtbetalt = Kroner(2000000),
        utbetaltPerMaaned = Kroner(16000),
    )

fun createEndringIOpptjeningAutoDtoBarnetilleggFellesbarn() =
    EndringIOpptjeningAutoDto.FellesbarnTillegg(
        beloepBrutto = Kroner(0),
        beloepNetto = Kroner(0),
        fribeloep = Kroner(0),
        gjelderFlereBarn = false,
        harFradrag = false,
        harFratrukketBeloepFraAnnenForelder = false,
        harJusteringsbeloep = false,
        inntektAnnenForelder = Kroner(0),
        inntektBruktIAvkortning = Kroner(0),
        inntektstak = Kroner(0),
    )

fun createEndringIOpptjeningAutoDtoBarnetilleggSaerkullsbarn() =
    EndringIOpptjeningAutoDto.SaerkullsbarnTillegg(
        beloepBrutto = Kroner(0),
        beloepNetto = Kroner(0),
        fribeloep = Kroner(0),
        gjelderFlereBarn = false,
        harFradrag = false,
        harJusteringsbeloep = false,
        inntektBruktIAvkortning = Kroner(0),
        inntektstak = Kroner(0),
    )