package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeper
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTO
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingDTO
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingPeriode
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingRedigerbartBrevDTO
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingResultat
import java.time.LocalDate
import java.time.Month

fun createTilbakekrevingRedigerbartBrevDTO() =  TilbakekrevingRedigerbartBrevDTO(
	innhold = createPlaceholderForRedigerbartInnhold(),
)

fun createTilbakekrevingFerdigDTO() =
	TilbakekrevingBrevDTO(
		innhold = createPlaceholderForRedigerbartInnhold(),
		sakType = SakType.OMSTILLINGSSTOENAD,
		brukerNavn = "Ola Nordmann",
		doedsbo = false,
		bosattUtland = false,
		varselVedlagt = true,
		datoVarselEllerVedtak = LocalDate.of(2024, Month.MARCH, 1),
		datoTilsvarBruker = LocalDate.of(2024, Month.MARCH, 15),
		tilbakekreving = TilbakekrevingDTO(
			fraOgMed = LocalDate.of(2024, Month.JANUARY, 1),
			tilOgMed = LocalDate.of(2024, Month.FEBRUARY, 28),
			skalTilbakekreve = false,
			helTilbakekreving = true,
			perioder = listOf(
				TilbakekrevingPeriode(
					maaned = LocalDate.of(2024, Month.JANUARY, 1),
					beloeper = TilbakekrevingBeloeper(
						feilutbetaling = Kroner(50),
						bruttoTilbakekreving = Kroner(50),
						nettoTilbakekreving = Kroner(35),
						fradragSkatt = Kroner(15),
						renteTillegg = Kroner(5),
						sumNettoRenter = Kroner(40)

					),
					resultat = TilbakekrevingResultat.FULL_TILBAKEKREV,
				),
				TilbakekrevingPeriode(
					maaned = LocalDate.of(2024, 2, 1),
					beloeper = TilbakekrevingBeloeper(
						feilutbetaling = Kroner(50),
						bruttoTilbakekreving = Kroner(50),
						nettoTilbakekreving = Kroner(35),
						fradragSkatt = Kroner(15),
						renteTillegg = Kroner(5),
						sumNettoRenter = Kroner(40)
					),
					resultat = TilbakekrevingResultat.FULL_TILBAKEKREV,
				)
			),
			summer = TilbakekrevingBeloeper(
				feilutbetaling = Kroner(100),
				bruttoTilbakekreving = Kroner(100),
				nettoTilbakekreving = Kroner(70),
				fradragSkatt = Kroner(30),
				renteTillegg = Kroner(10),
				sumNettoRenter = Kroner(80)
			)
		)
	)