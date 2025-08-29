package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU2423_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2423NN, TBU2423EN, TBU2423]

		paragraph {
			text (
				bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-5 til 12-7 og 12-10." },
				nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-5 til 12-7 og 12-10." },
				english { + "This decision is made pursuant to Sections 12-5 through 12-7 and 12-10 of the National Insurance Act." },
			)
		}
    }
}
