package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.IntBroek
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.ElementType
import no.nav.pensjon.etterlatte.maler.InnerElement
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkFerdigDTO

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

fun createBarnepensjonOmregnetNyttRegelverkFerdigDTO() =
    BarnepensjonOmregnetNyttRegelverkFerdigDTO(
        innhold = listOf(
            Element(
                type = ElementType.HEADING_TWO,
                children = listOf(
                    InnerElement(
                        text = "Tittel"
                    )
                )
            ),
            Element(
                type = ElementType.PARAGRAPH,
                children = listOf(
                    InnerElement(
                        text = "Paragraf"
                    )
                )
            )
        ),
        data = BarnepensjonOmregnetNyttRegelverkDTO(
            utbetaltFoerReform = Kroner(1337),
            utbetaltEtterReform = Kroner(31337),
            anvendtTrygdetid = 40,
            grunnbeloep = Kroner(400000),
            prorataBroek = IntBroek(43, 156),
            erBosattUtlandet = true,
            erYrkesskade = false
        )
    )