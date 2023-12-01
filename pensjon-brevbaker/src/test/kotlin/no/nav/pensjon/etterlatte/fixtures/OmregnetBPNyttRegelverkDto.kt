package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.IntBroek
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTO

fun createBarnepensjonOmregnetNyttRegelverkDTO() =
    BarnepensjonOmregnetNyttRegelverkDTO(
        utbetaltFoerReform = Kroner(1337),
        utbetaltEtterReform = Kroner(31337),
        anvendtTrygdetid = 40,
        grunnbeloep = Kroner(400000),
        prorataBroek = IntBroek(43, 156),
        erBosattUtlandet = true,
        erYrkesskade = false
    )