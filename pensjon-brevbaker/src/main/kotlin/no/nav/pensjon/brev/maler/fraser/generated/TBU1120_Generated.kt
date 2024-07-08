

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner


data class TBU1120_Generated(
	val beregningUfore_totalNetto: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1120NN, TBU1120, TBU1120EN]

		paragraph {
			textExpr (
				Bokmal to "Du får ".expr() + beregningUfore_totalNetto.format() + " kroner i uføretrygd per måned før skatt.",
				Nynorsk to "Du får ".expr() + beregningUfore_totalNetto.format() + " kroner i uføretrygd per månad før skatt.",
				English to "Your monthly disability benefit payment will be NOK ".expr() + beregningUfore_totalNetto.format() + " before tax.",
			)
		}
    }
}
        