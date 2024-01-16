package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadOpphoerDTO

fun createOmstillingsstoenadpphoerDTO() =
    OmstillingsstoenadOpphoerDTO(
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
        )
    )