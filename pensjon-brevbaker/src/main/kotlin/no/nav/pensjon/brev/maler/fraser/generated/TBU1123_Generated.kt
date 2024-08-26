

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


data class TBU1123_Generated(
    val beregningUfore_totalNetto: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1123NN, TBU1123, TBU1123EN]

		paragraph {
			textExpr (
				Bokmal to "Du får ".expr() + beregningUfore_totalNetto.format() + " kroner i uføretrygd, barne- og gjenlevendetillegg per måned før skatt.",
				Nynorsk to "Du får ".expr() + beregningUfore_totalNetto.format() + " kroner i uføretrygd, barne- og attlevandetillegg per månad før skatt.",
				English to "Your monthly disability benefit, child supplement and survivor's supplement payment will be NOK ".expr() + beregningUfore_totalNetto.format() + " before tax.",
			)
		}
    }
}
        