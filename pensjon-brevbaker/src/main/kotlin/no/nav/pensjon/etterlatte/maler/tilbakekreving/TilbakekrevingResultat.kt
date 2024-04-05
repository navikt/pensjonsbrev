package no.nav.pensjon.etterlatte.maler.tilbakekreving

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.StringExpression

enum class TilbakekrevingResultat {
	DELVIS_TILBAKEKREV,
	FEILREGISTRERT,
	FORELDET,
	FULL_TILBAKEKREV,
	INGEN_TILBAKEKREV,
}

object TilbakekrevingResultatFormatter : LocalizedFormatter<TilbakekrevingResultat>() {
	override fun apply(resultat: TilbakekrevingResultat, spraak: Language): String {
		return when (spraak) {
			Language.Bokmal -> when (resultat) {
				TilbakekrevingResultat.FULL_TILBAKEKREV-> "Full tilbakekreving"
				TilbakekrevingResultat.DELVIS_TILBAKEKREV -> "Delvis tilbakekreving"
				TilbakekrevingResultat.INGEN_TILBAKEKREV -> "Ingen tilbakekreving"
				TilbakekrevingResultat.FORELDET -> "Foreldet"
				TilbakekrevingResultat.FEILREGISTRERT -> "Feilregistrert"
			}

			Language.Nynorsk -> when (resultat) {
				TilbakekrevingResultat.FULL_TILBAKEKREV-> "Full tilbakekrevjing"
				TilbakekrevingResultat.DELVIS_TILBAKEKREV -> "Delvis tilbakekrevjing"
				TilbakekrevingResultat.INGEN_TILBAKEKREV -> "Ingen tilbakekrevjing"
				TilbakekrevingResultat.FORELDET -> "Forelda"
				TilbakekrevingResultat.FEILREGISTRERT -> "Feilregistrert"
			}

			Language.English -> when (resultat) {
				TilbakekrevingResultat.FULL_TILBAKEKREV-> "Full reimbursement"
				TilbakekrevingResultat.DELVIS_TILBAKEKREV -> "Partial reimbursement"
				TilbakekrevingResultat.INGEN_TILBAKEKREV -> "No reimbursement"
				TilbakekrevingResultat.FORELDET -> "Obsolete"
				TilbakekrevingResultat.FEILREGISTRERT -> "Incorrectly registered"
			}
		}
	}

	override fun stableHashCode(): Int = "TilbakekrevingResultatFormatter".hashCode()
}

fun Expression<TilbakekrevingResultat>.format(formatter: LocalizedFormatter<TilbakekrevingResultat> = TilbakekrevingResultatFormatter): StringExpression =
	Expression.BinaryInvoke(
		first = this,
		second = Expression.FromScope.Language,
		operation = formatter,
	)