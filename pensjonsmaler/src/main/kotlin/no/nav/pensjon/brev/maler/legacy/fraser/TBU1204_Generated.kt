package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU1204_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1204NN, TBU1204, TBU1204EN]

		paragraph {
			text (
				Bokmal to "Du har mulighet til å ha inntekt ved siden av uføretrygden din. Det lønner seg å jobbe, fordi inntekt og uføretrygd alltid vil være høyere enn uføretrygd alene.",
				Nynorsk to "Det er mogleg for deg å ha inntekt ved sida av uføretrygda di. Det lønner seg å jobbe fordi inntekt og uføretrygd alltid vil vere høgare enn uføretrygd åleine.",
				English to "You may earn an income at the same time as receiving disability benefit. It pays to work because income and disability benefit will always be higher than your disability benefit alone.",
			)
		}
    }
}
