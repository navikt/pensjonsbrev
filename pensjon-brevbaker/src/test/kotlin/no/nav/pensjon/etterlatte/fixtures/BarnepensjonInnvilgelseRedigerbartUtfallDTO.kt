package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregning
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiode
import no.nav.pensjon.etterlatte.maler.BeregningsMetode
import no.nav.pensjon.etterlatte.maler.IntBroek
import no.nav.pensjon.etterlatte.maler.Periode
import no.nav.pensjon.etterlatte.maler.Trygdetid
import no.nav.pensjon.etterlatte.maler.TrygdetidType
import no.nav.pensjon.etterlatte.maler.Trygdetidsperiode
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseRedigerbartUtfallDTO
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

fun createBarnepensjonInnvilgelseRedigerbartUtfallDTO() = BarnepensjonInnvilgelseRedigerbartUtfallDTO(
    avdoed = Avdoed(
        navn = "Avdoed Avdoedesen",
        doedsdato = LocalDate.now().minusMonths(1),
    ),
    beregning = BarnepensjonBeregning(
        innhold = listOf(),
        virkningsdato = LocalDate.now(),
        antallBarn = 2,
        grunnbeloep = Kroner(123456),
        beregningsperioder = listOf(
            BarnepensjonBeregningsperiode(
                datoFOM = LocalDate.of(2020, Month.JANUARY, 1),
                datoTOM = LocalDate.of(2023, Month.JULY, 31),
                grunnbeloep = Kroner(123456),
                antallBarn = 2,
                utbetaltBeloep = Kroner(6234)
            )
        ),
        sisteBeregningsperiode = BarnepensjonBeregningsperiode(
            datoFOM = LocalDate.of(2020, Month.JANUARY, 1),
            datoTOM = LocalDate.of(2023, Month.JULY, 31),
            grunnbeloep = Kroner(123456),
            antallBarn = 2,
            utbetaltBeloep = Kroner(6234)
        ),
        trygdetid = Trygdetid(
            trygdetidsperioder = listOf(
                Trygdetidsperiode(
                    datoFOM = LocalDate.of(2004, 1, 1),
                    datoTOM = LocalDate.of(2024, 1, 1),
                    land = "NOR",
                    opptjeningsperiode = Periode(20, 0, 0),
                    type = TrygdetidType.FAKTISK
                ),
                Trygdetidsperiode(
                    datoFOM = LocalDate.of(2024, 1, 1),
                    datoTOM = LocalDate.of(2044, 1, 1),
                    land = "NOR",
                    opptjeningsperiode = Periode(20, 0, 0),
                    type = TrygdetidType.FREMTIDIG
                )
            ),
            beregnetTrygdetidAar = 40,
            beregnetTrygdetidMaaneder = 480,
            prorataBroek = IntBroek(20, 150),
            beregningsMetodeFraGrunnlag = BeregningsMetode.NASJONAL,
            beregningsMetodeAnvendt = BeregningsMetode.NASJONAL,
            mindreEnnFireFemtedelerAvOpptjeningstiden = false,
        )
    ),
    erEtterbetaling = true,
    harFlereUtbetalingsperioder = false,
)