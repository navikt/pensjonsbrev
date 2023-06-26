package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.AdoptertAv
import no.nav.pensjon.etterlatte.maler.BarnepensjonRevurderingAdopsjonDTO
import java.time.LocalDate
import java.time.Month

fun createBarnepensjonRevurderingAdopsjonDTO() =
    BarnepensjonRevurderingAdopsjonDTO(
        virkningsdato = LocalDate.of(2023, Month.MARCH, 8),
        adoptertAv = AdoptertAv("Navn Navnesen"),
    )
