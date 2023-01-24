package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDto
import no.nav.pensjon.brev.maler.EndringOpptjeningAuto
import java.time.LocalDate

@Suppress("unused")
fun createEndringOpptjeningAutoDto() =
    EndringOpptjeningAutoDto(
        barnetilleggFellesbarn = Fixtures.create(),
        barnetilleggSaerkullsbarn = Fixtures.create(),
        endringIOpptjening = Fixtures.create(),
        kombinereUfoeretrygdMedInntekt = Fixtures.create(),
        ufoeretrygd = Fixtures.create(),
    )

fun createEndringOpptjeningAutoDtoEndringIOpptjening() =
    EndringOpptjeningAutoDto.EndringIOpptjening(
        ufoerBeloepOekt = false,
        ufoerBeloepRedusert = true,
        virkningsDato = LocalDate.of(2023, 1, 1),
    )

fun createEndringOpptjeningAutoDtoKombinereUfoeretrygdMedInntekt() =
    EndringOpptjeningAutoDto.KombinereUfoeretrygdMedInntekt(
        beloepsgrense = Kroner(0),
        forventetInntekt = Kroner(0),
        grunnbeloep = Kroner(111477),  // 1G 2023
        harBeloepOekt = false,
        harBeloepRedusert = false,
        harDelvisUfoeregrad = true,
        harFullUfoeregrad = false,
        harInntektEtterUfoere = false,
        inntektsgrense = Kroner(0),
        inntektsgrenseNesteAar = Kroner(0),
        inntektstak = Kroner(0),
        kompensasjonsgrad = 0,
        nettoAkkumulerteBeloepUtbetalt = Kroner(0),
        nettoAkkumulertePlussNettoRestAar = Kroner(0),
        nettoTilUtbetalingRestenAvAaret = Kroner(0),
        nettoUfoeretrygdUtbetaltPerMaaned = Kroner(0),
        oppjustertInntektEtterUfoere = Kroner(0),
        oppjustertInntektFoerUfoere80prosent = Kroner(0),
        oppjustertInntektFoerUfoere = Kroner(0),
        ufoeregrad = 50,
        utbetalingsgrad = 50,
    )

fun createEndringOpptjeningAutoDtoUfoeretrygd() =
    EndringOpptjeningAutoDto.Ufoeretrygd(
        harEktefelletilleggInnvilget = true,
        harFellesbarnInnvilget = true,
        harGjenlevendetilleggInnvilget = false,
        harUtbetalingsgrad = true,
        harYrkesskadeGradUtbetaling = false,
        harSaerkullsbarnInnvilget = false,
        ufoertrygdUtbetalt = Kroner(2000000),
        utbetaltPerMaaned = Kroner(16000),
    )

fun createEndringOpptjeningAutoDtoBarnetilleggFellesbarn() =
    EndringOpptjeningAutoDto.BarnetilleggFellesbarn(
        beloepBrutto = Kroner(24000)
    )

fun createEndringOpptjeningAutoDtoBarnetilleggSaerkullsbarn() =
    EndringOpptjeningAutoDto.BarnetilleggSaerkullsbarn(
        beloepBrutto = Kroner(34000)
    )