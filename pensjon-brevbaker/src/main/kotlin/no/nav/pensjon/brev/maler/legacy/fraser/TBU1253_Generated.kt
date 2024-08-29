

package no.nav.pensjon.brev.maler.legacy.fraser

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


data class TBU1253_Generated(
    val beregningUfore_totalNetto: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1253NN, TBU1253, TBU1253EN]

		paragraph {
			textExpr (
				Bokmal to "Du får ".expr() + beregningUfore_totalNetto.format() + " kroner i uføretrygd og ektefelletillegg per måned før skatt.",
				Nynorsk to "Du får ".expr() + beregningUfore_totalNetto.format() + " kroner i uføretrygd og ektefelletillegg per månad før skatt.",
				English to "Your monthly disability benefit and spouse supplement payment will be NOK ".expr() + beregningUfore_totalNetto.format() + " before tax.",
			)
		}
    }
}
        