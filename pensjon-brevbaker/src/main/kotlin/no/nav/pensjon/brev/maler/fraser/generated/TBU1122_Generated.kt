

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*


data class TBU1122_Generated(
    val beregningUfore_totalNetto: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1122NN, TBU1122, TBU1122EN]

		paragraph {
			textExpr (
				Bokmal to "Du får ".expr() + beregningUfore_totalNetto.format() + " kroner i uføretrygd og gjenlevendetillegg per måned før skatt.",
				Nynorsk to "Du får ".expr() + beregningUfore_totalNetto.format() + " kroner i uføretrygd og attlevandetillegg per månad før skatt.",
				English to "Your monthly disability benefit and survivor's supplement payment will be NOK ".expr() + beregningUfore_totalNetto.format() + " before tax.",
			)
		}
    }
}
        