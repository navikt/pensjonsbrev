package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeper
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingFerdigDTO
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingInnholdDTO
import java.time.LocalDate

fun createTilbakekrevingInnholdDTO() = TilbakekrevingInnholdDTO(
    sakType = SakType.OMSTILLINGSSTOENAD,
    skalBetaleTilbake = true,
    bosattUtland = false,
    varselVedlagt = false,
    datoVarselEllerVedtak = LocalDate.now(),
    tilsvarfraBruker = false,
    datoTilsvarBruker = null,
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