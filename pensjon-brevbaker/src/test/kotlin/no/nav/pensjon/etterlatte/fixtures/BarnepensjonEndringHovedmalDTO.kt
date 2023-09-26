package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Beregningsperiode
import no.nav.pensjon.etterlatte.maler.EtterbetalingDTO
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.EndringHovedmalDTO
import java.time.LocalDate
import java.time.Month

fun createEndringHovedmalDTO() = EndringHovedmalDTO(
    erEndret = true,
    etterbetaling = EtterbetalingDTO(
        fraDato = LocalDate.of(2020, Month.JANUARY, 1),
        tilDato = LocalDate.of(2023, Month.DECEMBER, 31),
    ),
    utbetalingsinfo = Utbetalingsinfo(
        antallBarn = 2,
        beloep = Kroner(1234),
        soeskenjustering = true,
        virkningsdato = LocalDate.now(),
        beregningsperioder = listOf(
            Beregningsperiode(
                datoFOM = LocalDate.now(),
                datoTOM = LocalDate.now(),
                grunnbeloep = Kroner(106003),
                antallBarn = 1,
                utbetaltBeloep = Kroner(495),
            ),
            Beregningsperiode(
                datoFOM = LocalDate.now(),
                datoTOM = null,
                grunnbeloep = Kroner(106003),
                antallBarn = 1,
                utbetaltBeloep = Kroner(495),
            ),
        ),
    ),
    innhold = listOf(),
)
