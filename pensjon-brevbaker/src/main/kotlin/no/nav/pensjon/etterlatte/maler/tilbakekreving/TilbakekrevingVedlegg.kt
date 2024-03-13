package no.nav.pensjon.etterlatte.maler.tilbakekreving

import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingDTOSelectors.perioder
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingDTOSelectors.summer

@TemplateModelHelpers
val tilbakekrevingVedlegg = createAttachment(
	title = newText(
		Bokmal to "Oversikt over feiltutbetalinger",
		Nynorsk to "Oversikt over feiltutbetalinger",
		English to "Oversikt over feiltutbetalinger",
	),
	includeSakspart = false
) {
	title2 {
		text(
			Bokmal to "Oversikt over feiltutbetalinger",
			Nynorsk to "Oversikt over feiltutbetalinger",
			English to "Oversikt over feiltutbetalinger",
		)
	}
	includePhrase(TilbakekrevingVedleggFraser.OversiktOverFeilutbetalinger(summer))

	title2 {
		text(
			Bokmal to "Detaljert oversikt over perioder med feilutbetaling",
			Nynorsk to "",
			English to "",
		)
	}
	paragraph {
		text(
			Bokmal to "Alle beløp er i norske kroner. Eventuelle renter kommer i tillegg, " +
					"se “Rentetillegg” i tabellen over. Brutto tilbakekreving er før fradrag for skatt, " +
					"mens netto tilbakekreving er etter fradrag for skatt.",
			Nynorsk to "",
			English to "",
		)
	}
	includePhrase(TilbakekrevingVedleggFraser.PeriodeTabell(perioder))

}
