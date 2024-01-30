package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerDTO

fun createOmstillingsstoenadOpphoerDTO() =
    OmstillingsstoenadOpphoerDTO(
        innhold = listOf(
            Element(
                type = ElementType.HEADING_TWO,
                children = listOf(
                    InnerElement(
                        text = "Begrunnelse for vedtak"
                    )
                )
            ),
            Element(
                type = ElementType.PARAGRAPH,
                children = listOf(
                    InnerElement(
                        text = "Her kommer det en begrunnelse"
                    )
                )
            )
        ),
        bosattUtland = false
    )