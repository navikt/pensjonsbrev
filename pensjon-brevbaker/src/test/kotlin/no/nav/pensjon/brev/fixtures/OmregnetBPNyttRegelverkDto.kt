package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTO

fun createBarnepensjonOmregnetNyttRegelverkDTO() =
    BarnepensjonOmregnetNyttRegelverkDTO(
        utbetaltFoerReform = 1337, utbetaltEtterReform = 31337, anvendtTrygdetid = 40
    )