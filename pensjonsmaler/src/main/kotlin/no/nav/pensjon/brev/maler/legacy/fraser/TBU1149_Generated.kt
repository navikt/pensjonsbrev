package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU1149_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1149EN, TBU1149, TBU1149NN]

		paragraph {
			text (
				bokmal { + "Rettighet som ung ufør" },
				nynorsk { + "Rett som ung ufør" },
				english { + "Rights as a young disabled person" },
			)
		}
    }
}
