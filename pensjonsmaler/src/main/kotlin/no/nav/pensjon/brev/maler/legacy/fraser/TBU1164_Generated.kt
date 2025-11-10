package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU1164_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1164NN, TBU1164EN, TBU1164]

		paragraph {
			text (
				bokmal { + "Uførhet som skyldes yrkesskade eller yrkessykdom" },
				nynorsk { + "Uførleik som kjem av yrkesskade eller yrkessjukdom" },
				english { + "Disability due to occupational injury or illness" },
			)
		}
    }
}
