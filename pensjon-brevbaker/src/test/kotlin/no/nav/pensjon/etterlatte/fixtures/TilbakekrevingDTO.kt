package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.ElementType
import no.nav.pensjon.etterlatte.maler.InnerElement
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingFerdigDTO
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingInnholdDTO

fun createTilbakekrevingInnholdDTO() = TilbakekrevingInnholdDTO(
    harRenter = true
)

fun createTilbakekrevingFerdigDTO() =
    TilbakekrevingFerdigDTO(
        innhold = listOf(
            Element(
                type = ElementType.HEADING_TWO,
                children = listOf(
                    InnerElement(
                        text = "Tittel 2"
                    )
                )
            ),
            Element(
                type = ElementType.PARAGRAPH,
                children = listOf(
                    InnerElement(
                        text = "Her kommer det valgfri tekst om innvilget vedtak"
                    )
                )
            )
        ),
        erOMS = true,
        erBP = false
    )