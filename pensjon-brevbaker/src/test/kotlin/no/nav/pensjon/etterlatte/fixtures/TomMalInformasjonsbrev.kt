package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.*

fun createTomMalInformasjonsbrev() =
    ManueltBrevDTO(
        innhold = emptyList(),
        tittel = "Dette er et informasjonsbrev"
    )