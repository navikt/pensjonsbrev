package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

data class TBU2443_Generated(val uniqueness: String? = null) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2443NN, TBU2443EN, TBU2443]

		paragraph(uniqueness = uniqueness) {
			text (
				bokmal { + "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om økt uføretrygd." },
				nynorsk { + "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om auka uføretrygd." },
				english { + "You do not meet the requirements, and your application for increased disability benefit is thus denied." },
			)
		}
    }
}
