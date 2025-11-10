package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU1223_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1223EN, TBU1223NN, TBU1223]

		paragraph {
			text (
				bokmal { + "Du må melde fra om endringer" },
				nynorsk { + "Du må melde frå om endringar" },
				english { + "You must notify any changes" },
			)
		}
    }
}
