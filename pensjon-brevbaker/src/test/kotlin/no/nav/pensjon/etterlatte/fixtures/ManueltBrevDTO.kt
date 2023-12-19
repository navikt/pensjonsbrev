package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.ElementType
import no.nav.pensjon.etterlatte.maler.InnerElement
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTO

fun createManueltBrevDTO() =
    ManueltBrevDTO(
        listOf(
            Element(
                type = ElementType.HEADING_TWO,
                children = listOf(
                    InnerElement(
                        text = "Tittel 2"
                    )
                )
            ),
            Element(
                type = ElementType.HEADING_THREE,
                children = listOf(
                    InnerElement(
                        text = "Tittel 3"
                    )
                )
            ),
            Element(
                type = ElementType.PARAGRAPH,
                children = listOf(
                    InnerElement(
                        text = "Text med mye info 1"
                    ),
                    InnerElement(
                        text = "Text med mye info 2"
                    ),
                    InnerElement(
                        text = "Text med mye info 3"
                    )
                )
            ),
            Element(
                type = ElementType.BULLETED_LIST,
                children = listOf(
                    InnerElement(
                        type = ElementType.LIST_ITEM,
                        children = listOf(
                            InnerElement(text = "Listeting 1 "),
                            InnerElement(text = "med ekstra "),
                            InnerElement(text = "tekst!"),
                        )
                    ),
                    InnerElement(
                        type = ElementType.LIST_ITEM,
                        children = listOf(
                            InnerElement(text = "Listeting 2")
                        )
                    ),
                    InnerElement(
                        type = ElementType.LIST_ITEM,
                        children = listOf(
                            InnerElement(text = "Listeting 3")
                        )
                    )
                )
            )
        )
    )