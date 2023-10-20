package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingDTO

fun createTilbakekrevingDTO() =
    TilbakekrevingDTO(
        erOMS = true,
        erBP = false
    )