package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.Navn
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingAdopsjonDTO
import java.time.LocalDate
import java.time.Month

fun createBarnepensjonRevurderingAdopsjonDTO() =
    BarnepensjonRevurderingAdopsjonDTO(
        virkningsdato = LocalDate.of(2023, Month.MARCH, 8),
        adoptertAv1 = Navn(fornavn = "Navn", mellomnavn = "Navnish", etternavn = "Navnesen"),
        adoptertAv2 = Navn(fornavn = "Naveen", etternavn = "Navnesen-ish"),
    )
