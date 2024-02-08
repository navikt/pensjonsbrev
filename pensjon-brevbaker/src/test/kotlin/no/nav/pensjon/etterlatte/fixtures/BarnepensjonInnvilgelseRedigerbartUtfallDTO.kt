package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseRedigerbartUtfallDTO
import java.time.LocalDate
import java.time.Month

fun createBarnepensjonInnvilgelseRedigerbartUtfallDTO() = BarnepensjonInnvilgelseRedigerbartUtfallDTO(
    virkningsdato = LocalDate.of(2020, Month.JANUARY, 1),
    avdoed = Avdoed(
        navn = "Avdoed Avdoedesen",
        doedsdato = LocalDate.now().minusMonths(1),
    ),
    sisteBeregningsperiodeDatoFom = LocalDate.of(2020, Month.JANUARY, 1),
    sisteBeregningsperiodeBeloep = Kroner(1000),
    erEtterbetaling = true,
    harFlereUtbetalingsperioder = false,
)