package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU3103_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU3103EN, TBU3103NN, TBU3103]

		paragraph {
			text (
				bokmal { + "Utbetaling av uføretrygd for deg som er innlagt på institusjon" },
				nynorsk { + "Utbetaling av uføretrygd når du er innlagd på institusjon" },
				english { + "Payment of disability benefit when you have been admitted to an institution" },
			)
		}
    }
}
