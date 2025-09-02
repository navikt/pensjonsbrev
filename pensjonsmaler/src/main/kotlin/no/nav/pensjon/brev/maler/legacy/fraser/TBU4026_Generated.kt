package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU4026_Generated : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU4026_NN, TBU4026]

		paragraph {
			text (
				bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 12-16. " },
				nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 12-16. " },
			)
		}
    }
}
