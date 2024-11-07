package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU1203_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1203NN, TBU1203, TBU1203EN]

		title1 {
			text (
				Bokmal to "For deg som kombinerer uføretrygd og inntekt",
				Nynorsk to "For deg som kombinerer uføretrygd og inntekt",
				English to "If you are combining disability benefit and income",
			)
		}
    }
}
