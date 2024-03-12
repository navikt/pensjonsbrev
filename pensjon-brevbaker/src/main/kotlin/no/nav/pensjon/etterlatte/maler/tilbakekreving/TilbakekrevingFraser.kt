package no.nav.pensjon.etterlatte.maler.tilbakekreving


import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.fraser.common.format
import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import java.time.LocalDate

object TilbakekrevingFraser {

	data class ViserTilVarselbrev(
		val sakType: Expression<SakType>,
		val varselVedlagt: Expression<Boolean>,
		val datoVarselEllerVedtak: Expression<LocalDate>,
		val datoTilsvarBruker: Expression<LocalDate?>
	) : OutlinePhrase<LangBokmalNynorskEnglish>() {
		override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
			paragraph {
				showIf(varselVedlagt) {
					textExpr(
						Bokmal to "Vi viser til forhåndsvarselet vårt som fulgte vedtak datert ".expr() +
								datoVarselEllerVedtak.format(),
						Nynorsk to "Vi viser til forhåndsvarselet vårt som fulgte vedtak datert ".expr() +
								datoVarselEllerVedtak.format(),
						English to "Vi viser til forhåndsvarselet vårt som fulgte vedtak datert ".expr() +
								datoVarselEllerVedtak.format(),
					)
				}.orShow {
					textExpr(
						Bokmal to "Vi viser til forhåndsvarselet vårt om at vi vurderer om du må betale tilbake ".expr() +
								sakType.format() + " av " + datoVarselEllerVedtak.format(),
						Nynorsk to "Vi viser til forhåndsvarselet vårt om at vi vurderer om du må betale tilbake ".expr() +
								sakType.format() + " av " + datoVarselEllerVedtak.format(),
						English to "Vi viser til forhåndsvarselet vårt om at vi vurderer om du må betale tilbake ".expr() +
								sakType.format() + " av " + datoVarselEllerVedtak.format(),
					)
				}
				ifNotNull(datoTilsvarBruker) { datoTilsvarBruker ->
					textExpr(
						Bokmal to ", og dine kommentarer til dette som vi mottok den".expr() + datoTilsvarBruker.format(),
						Nynorsk to ", og dine kommentarer til dette som vi mottok den".expr() + datoTilsvarBruker.format(),
						English to ", og dine kommentarer til dette som vi mottok den".expr() + datoTilsvarBruker.format(),
					)
				}
				text(
					Bokmal to ".",
					Nynorsk to ".",
					English to ".",
				)
			}
		}
	}

}