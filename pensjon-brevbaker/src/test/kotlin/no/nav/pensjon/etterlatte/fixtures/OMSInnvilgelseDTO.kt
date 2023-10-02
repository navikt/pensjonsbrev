package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.OMSInnvilgelseDTO
import java.time.LocalDate

fun createOMSInnvilgelseDTO() =
    OMSInnvilgelseDTO(
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
                )
            )
        ),
        avkortingsinfo = Avkortingsinfo(
            grunnbeloep = Kroner(118000),
            inntekt = Kroner(550000),
            virkningsdato = LocalDate.now(),
            beregningsperioder = listOf(
                AvkortetBeregningsperiode(
                    datoFOM = LocalDate.now(),
                    datoTOM = LocalDate.now(),
                    inntekt = Kroner(550000),
                    utbetaltBeloep = Kroner(0),
                    ytelseFoerAvkorting = Kroner(100),
                    trygdetid = 40
                ),
                AvkortetBeregningsperiode(
                    datoFOM = LocalDate.now(),
                    datoTOM = null,
                    inntekt = Kroner(550000),
                    utbetaltBeloep = Kroner(0),
                    ytelseFoerAvkorting = Kroner(100),
                    trygdetid = 40
                )
            )
        ),
        avdoed = Avdoed(
            navn = "Avdoed Avdoedesen",
            doedsdato = LocalDate.now()
        ),
        etterbetalingsinfo = EtterbetalingDTO(
            fraDato = LocalDate.now(),
            tilDato = LocalDate.now()
        )
    )