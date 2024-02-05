package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.ElementType
import no.nav.pensjon.etterlatte.maler.InnerElement

fun createPlaceholderForRedigerbartInnhold(): List<Element> =
    listOf(
        Element(
            type = ElementType.HEADING_THREE,
            children = listOf(
                InnerElement(
                    text = "Redigerbart innhold"
                )
            )
        ),
        Element(
            type = ElementType.PARAGRAPH,
            children = listOf(
                InnerElement(
                    text = "Her kommer det redigerbart innhold"
                )
            )
        )
    )
