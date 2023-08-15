package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Beregningsperiode
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.BarnepensjonEndringInstitusjonsoppholdDTO
import java.time.LocalDate
import java.time.Month

fun createBarnepensjonEndringInstitusjonsoppholdDTO() = BarnepensjonEndringInstitusjonsoppholdDTO(
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
    erEtterbetalingMerEnnTreMaaneder = false,
    virkningsdato = LocalDate.of(2020, Month.APRIL, 15),
    prosent = 15,
    innlagtdato = LocalDate.of(2021, Month.MAY, 8),
    utskrevetdato = LocalDate.of(2022, Month.JUNE, 10),
)
