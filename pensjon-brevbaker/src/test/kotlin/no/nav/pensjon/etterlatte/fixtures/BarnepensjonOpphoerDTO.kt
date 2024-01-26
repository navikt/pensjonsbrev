package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerDTO

fun createBarnepensjonOpphoerDTO() = BarnepensjonOpphoerDTO(
    innhold = emptyList(),
    bosattUtland = true,
    brukerUnder18Aar = true,
)