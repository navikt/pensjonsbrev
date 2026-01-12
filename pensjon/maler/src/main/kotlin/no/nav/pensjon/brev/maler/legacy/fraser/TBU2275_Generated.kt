package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU2275_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2275NN, TBU2275, TBU2275EN]

		title1 {
			text (
				bokmal { + "For deg som mottar ektefelletillegg" },
				nynorsk { + "For deg som får ektefelletillegg" },
				english { + "For those receiving a spouse supplement" },
			)
		}
    }
}
