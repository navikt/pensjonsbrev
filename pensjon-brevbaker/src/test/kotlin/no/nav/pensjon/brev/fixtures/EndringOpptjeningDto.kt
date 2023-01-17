package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDto
import java.time.LocalDate

@Suppress("unused")
fun createEndringOpptjeningAutoDto() =
    EndringOpptjeningAutoDto(
        barnetilleggFellesbarn = Fixtures.create(),
        barnetilleggSaerkullsbarn = Fixtures.create(),
        endringIOpptjening = Fixtures.create(),
        ufoeretrygd = Fixtures.create(),
    )

fun createEndringOpptjeningAutoDtoEndringIOpptjening() =
    EndringOpptjeningAutoDto.EndringIOpptjening(
        ufoerBeloepOekt = false,
        ufoerBeloepRedusert = true,
        virkningsDato = LocalDate.of(2023, 1, 1),
    )

fun createEndringOpptjeningAutoDtoUfoeretrygd() =
    EndringOpptjeningAutoDto.Ufoeretrygd(
        ektefelletilleggInnvilget = true,
        fellesbarnInnvilget = true,
        gjenlevendetilleggInnvilget = false,
        harUtbetalingsgrad = true,
        harYrkesskadeGradUtbetaling = false,
        saerkullsbarnInnvilget = false,
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