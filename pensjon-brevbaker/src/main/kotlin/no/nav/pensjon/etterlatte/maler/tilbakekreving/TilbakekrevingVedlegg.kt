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
		Bokmal to "Oversikt over feilutbetalinger",
		Nynorsk to "Oversikt over feilutbetalingar",
		English to "Overview of payment errors",
	),
	includeSakspart = false
) {
	title2 {
		text(
			Bokmal to "Oversikt over feilutbetalinger",
			Nynorsk to "Oversikt over feilutbetalingar",
			English to "Overview of payment errors",
		)
	}
	includePhrase(TilbakekrevingVedleggFraser.OversiktOverFeilutbetalinger(summer))

	title2 {
		text(
			Bokmal to "Detaljert oversikt over perioder med feilutbetaling",
			Nynorsk to "Detaljert oversikt over periodar med feilutbetaling",
			English to "Detailed overview of periods with payment errors",
		)
	}
	paragraph {
		text(
			Bokmal to "Alle beløp er i norske kroner. Eventuelle renter kommer i tillegg, " +
					"se «Rentetillegg» i tabellen over. Brutto tilbakekreving er før fradrag for skatt, " +
					"mens netto tilbakekreving er etter fradrag for skatt.",
			Nynorsk to "Alle beløp er i norske kroner. Eventuelle renter kjem i tillegg (sjå «Rentetillegg» " +
					"i tabellen over). Brutto tilbakekrevjing er før frådrag for skatt, medan netto tilbakekrevjing " +
					"er etter frådrag for skatt.",
			English to "All amounts are in NOK. Any interest accrued will be added, see “Interest” in the table " +
					"above. Gross reimbursement refers to the deduction before tax, whereas net reimbursement " +
					"refers to the deduction after tax.",
		)
	}
	includePhrase(TilbakekrevingVedleggFraser.PeriodeTabell(perioder))

}
