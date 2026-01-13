package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU2364_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2364NN, TBU2364, TBU2364EN]

		title1 {
			text (
				bokmal { + "Du må melde fra om eventuell inntekt" },
				nynorsk { + "Du må melde frå om eventuell inntekt" },
				english { + "Report any income" },
			)
		}
    }
}
