package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU2366_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2366NN, TBU2366, TBU2366EN]

		title1 {
			text (
				bokmal { + "Du må melde fra om endringer i inntekten" },
				nynorsk { + "Du må melde frå om endringar i inntekta" },
				english { + "You must report all changes in income" },
			)
		}
    }
}
