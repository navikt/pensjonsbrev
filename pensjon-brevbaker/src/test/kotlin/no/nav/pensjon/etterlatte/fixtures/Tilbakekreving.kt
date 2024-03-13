package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeper
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTO
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingDTO
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingReidgerbartBrevDTO
import java.time.LocalDate

fun createTilbakekrevingReidgerbartBrevDTO() =  TilbakekrevingReidgerbartBrevDTO(
	innhold = createPlaceholderForRedigerbartInnhold(),
)

fun createTilbakekrevingFerdigDTO() =
	TilbakekrevingBrevDTO(
		innhold = createPlaceholderForRedigerbartInnhold(),
		sakType = SakType.OMSTILLINGSSTOENAD,
		skalTilbakekreve = true,
		helTilbakekreving = true,
		bosattUtland = false,
		varselVedlagt = false,
		datoVarselEllerVedtak = LocalDate.now(),
		datoTilsvarBruker = null,
		tilbakekreving = TilbakekrevingDTO(
			fraOgMed = LocalDate.now(),
			tilOgMed = LocalDate.now(),
			perioder = emptyList(),
			summer = TilbakekrevingBeloeper(
				feilutbetaling = Kroner(100),
				bruttoTilbakekreving = Kroner(100),
				nettoTilbakekreving = Kroner(70),
				fradragSkatt = Kroner(20),
				renteTillegg = Kroner(10),
				harRenteTillegg = true
			)
		)
	)