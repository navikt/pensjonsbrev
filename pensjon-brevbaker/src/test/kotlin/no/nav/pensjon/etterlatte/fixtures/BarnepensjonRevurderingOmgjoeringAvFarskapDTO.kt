package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.Navn
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingOmgjoeringAvFarskapDTO
import java.time.LocalDate
import java.time.Month

fun createBarnepensjonRevurderingOmgjoeringAvFarskapDTO() = BarnepensjonRevurderingOmgjoeringAvFarskapDTO(
    vedtaksdato = LocalDate.of(2023, Month.MAY, 15),
    virkningsdato = LocalDate.of(2023, Month.MAY, 1),
    naavaerendeFar = Navn(
        fornavn = "Peder",
        etternavn = "Ås",
    ),
    forrigeFar = Navn(
        fornavn = "Lars",
        etternavn = "Holm",
    ),
    forrigeVirkningsdato = LocalDate.of(2022, Month.JUNE, 1),
)
