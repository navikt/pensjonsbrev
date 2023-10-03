package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.Beregningsperiode
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.ny.BarnepensjonInnvilgelseEnkelDTO
import java.time.LocalDate

fun createBarnepensjonInnvilgelseEnkelDTO() = BarnepensjonInnvilgelseEnkelDTO(
    utbetalingsinfo = Utbetalingsinfo(
        antallBarn = 2,
        beloep = Kroner(1234),
        soeskenjustering = true,
        virkningsdato = LocalDate.now(),
        beregningsperioder = listOf(
            Beregningsperiode(
                datoFOM = LocalDate.now().minusMonths(1),
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
    avdoed = Avdoed(
        navn = "Avdoed Avdoedesen",
        doedsdato = LocalDate.now().minusMonths(1),
    ),
    vedtaksdato = LocalDate.now(),
    erEtterbetaling = true,
    erInstitusjonsopphold = true,
)
