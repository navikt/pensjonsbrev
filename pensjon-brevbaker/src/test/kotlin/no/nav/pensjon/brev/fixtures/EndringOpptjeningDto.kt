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
        folketrygdloven = Fixtures.create(),
        ufoeretrygd = Fixtures.create(),
        virkningsDato = LocalDate.of(2023, 1, 1)
    )

fun createEndringOpptjeningAutoDtoEndringIOpptjening() =
    EndringOpptjeningAutoDto.EndringIOpptjening(
        ufoerBeloepOekt = true,
        ufoerBeloepRedusert = false
    )

fun createEndringOpptjeningAutoDtoFolketrygdloven() =
    EndringOpptjeningAutoDto.Folketrygdloven(
        harYrkesskadeGradUtbetaling = false,
        innvilgetEktefelletillegg = true,
        innvilgetFellesbarntillegg = false,
        innvilgetGjenlevendetillegg = false,
        innvilgetSaerkullsbarntillegg = false
    )

fun createEndringOpptjeningAutoDtoUfoeretrygd() =
    EndringOpptjeningAutoDto.Ufoeretrygd(
        ektefelletilleggUtbeltalt = Kroner(25000),
        gjenlevendetilleggUtbetalt = Kroner(0),
        harUtbetalingsgrad = false,
        ufoertrygdUtbetalt = Kroner(2000000),
        utbetaltPerMaaned = Kroner(16000)
    )

fun createEndringOpptjeningAutoDtoBarnetilleggFellesbarn() =
    EndringOpptjeningAutoDto.BarnetilleggFellesbarn(
        beloepBrutto = Kroner(24000)
    )

fun createEndringOpptjeningAutoDtoBarnetilleggSaerkullsbarn() =
    EndringOpptjeningAutoDto.BarnetilleggSaerkullsbarn(
        beloepBrutto = Kroner(34000)
    )