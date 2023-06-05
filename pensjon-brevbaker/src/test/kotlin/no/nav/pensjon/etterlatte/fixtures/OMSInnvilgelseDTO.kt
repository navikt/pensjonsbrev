package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.OMSInnvilgelseDTO
import java.time.LocalDate

fun createOMSInnvilgelseDTO() =
    OMSInnvilgelseDTO(
        utbetalingsinfo = OMSInnvilgelseDTO.Utbetalingsinfo(
            beloep = Kroner(1234),
            virkningsdato = LocalDate.now(),
            grunnbeloep = Kroner(106003),
            inntekt = Kroner(856003),
            beregningsperioder = listOf(
                OMSInnvilgelseDTO.Beregningsperiode(
                    datoFOM = LocalDate.now(),
                    datoTOM = LocalDate.now(),
                    inntekt = Kroner(856003),
                    utbetaltBeloep = Kroner(0),
                ),
                OMSInnvilgelseDTO.Beregningsperiode(
                    datoFOM = LocalDate.now(),
                    datoTOM = null,
                    inntekt = Kroner(856003),
                    utbetaltBeloep = Kroner(0),
                )
            )
        ),
        avdoed = Avdoed(
            navn = "Avdoed Avdoedesen",
            doedsdato = LocalDate.now()
        )
    )