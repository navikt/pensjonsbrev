package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU1296_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1296, TBU1296NN, TBU1296EN]

		paragraph {
			text (
				Bokmal to "Du kan ha en årlig inntekt på 60 000 kroner uten at uføretrygden din blir redusert. Dette er inntektsgrensen din.",
				Nynorsk to "Du kan ha ei årleg inntekt på 60 000 kroner utan at uføretrygda di blir redusert. Dette er inntektsgrensa di.",
				English to "You may earn an annual income of up to NOK 60 000 without your disability benefit being reduced. This is your income limit.",
			)
		}
    }
}
