package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_totalnetto
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.formatMonthYear
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class TBU1254_Generated(
	val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1254NN, TBU1254, TBU1254EN]

		paragraph {
			textExpr (
				Bokmal to "Du får ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd, barne- og ektefelletillegg per måned før skatt",
				Nynorsk to "Du får ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd, barne- og ektefelletillegg per månad før skatt",
				English to "Your monthly disability benefit, child supplement and spouse supplement payment will be NOK ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " before tax",
			)
			ifNotNull(pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom()) {
				textExpr (
					Bokmal to " fra ".expr() + it.formatMonthYear(),
					Nynorsk to " frå ".expr() + it.formatMonthYear() ,
					English to " starting ".expr() + it.formatMonthYear(),
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
        