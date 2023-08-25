package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonFengselsoppholdDTO
import java.time.LocalDate
import java.time.Month

fun createBarnepensjonFengselsoppholdDTO() = BarnepensjonFengselsoppholdDTO(
    virkningsdato = LocalDate.of(2020, Month.JANUARY, 1),
    fraDato = LocalDate.of(2021, Month.JULY, 1),
    tilDato = LocalDate.of(20221, Month.JULY, 28),
)
