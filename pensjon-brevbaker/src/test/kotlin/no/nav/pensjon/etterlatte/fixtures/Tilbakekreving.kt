package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.ElementType
import no.nav.pensjon.etterlatte.maler.InnerElement
import no.nav.pensjon.etterlatte.maler.tilbakekreving.SakType
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeper
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingFerdigDTO
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingInnholdDTO

fun createTilbakekrevingInnholdDTO() = TilbakekrevingInnholdDTO(
    sakType = SakType.OMSTILLINGSSTOENAD,
    harRenter = true,
    harStrafferettslig = true,
    harForeldelse = true,
    perioder = emptyList(),
    summer = TilbakekrevingBeloeper(
        feilutbetaling = Kroner(100),
        bruttoTilbakekreving = Kroner(100),
        nettoTilbakekreving = Kroner(70),
        fradragSkatt = Kroner(20),
        renteTillegg = Kroner(10)
    )
)

fun createTilbakekrevingFerdigDTO() =
    TilbakekrevingFerdigDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        data = createTilbakekrevingInnholdDTO()
    )