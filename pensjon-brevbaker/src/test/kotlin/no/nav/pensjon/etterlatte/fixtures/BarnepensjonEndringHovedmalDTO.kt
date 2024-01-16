package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Beregningsperiode
import no.nav.pensjon.etterlatte.maler.Etterbetaling
import no.nav.pensjon.etterlatte.maler.Periode
import no.nav.pensjon.etterlatte.maler.TrygdetidType
import no.nav.pensjon.etterlatte.maler.Trygdetidsperiode
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.EndringHovedmalDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BeregningsMetode
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BeregningsinfoBP
import java.time.LocalDate
import java.time.Month

fun createEndringHovedmalDTO() = EndringHovedmalDTO(
    erEndret = true,
    etterbetaling = Etterbetaling(
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
    beregningsinfo = BeregningsinfoBP(
        innhold = listOf(),
        grunnbeloep = Kroner(123456),
        beregningsperioder = listOf(
            Beregningsperiode(
                datoFOM = LocalDate.of(2020, Month.JANUARY, 1),
                datoTOM = LocalDate.of(2023, Month.JULY, 31),
                grunnbeloep = Kroner(123456),
                antallBarn = 4,
                utbetaltBeloep = Kroner(6234)
            )
        ),
        antallBarn = 4,
        aarTrygdetid = 12,
        maanederTrygdetid = 3,
        prorataBroek = null,
        trygdetidsperioder = listOf(
            Trygdetidsperiode(
                datoFOM = LocalDate.of(2020, Month.JANUARY, 1),
                datoTOM = LocalDate.of(2023, Month.JULY, 31),
                land = "Albania",
                opptjeningsperiode = Periode(3, 0, 0),
                type = TrygdetidType.FAKTISK
            )
        ),
        beregningsMetodeAnvendt = BeregningsMetode.NASJONAL,
        beregningsMetodeFraGrunnlag = BeregningsMetode.NASJONAL,
    )
)
